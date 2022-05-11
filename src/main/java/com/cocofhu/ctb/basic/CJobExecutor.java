package com.cocofhu.ctb.basic;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.kernel.core.config.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.config.CDefaultDefaultWritableDataSet;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobDetail;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobParam;
import com.cocofhu.ctb.kernel.anno.param.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.config.CPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.exec.CExecutorJob;
import com.cocofhu.ctb.kernel.core.exec.CSimpleExecutor;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobSummary;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.CJobParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.CUnsupportedOperationException;

import java.util.*;

public class CJobExecutor {


    private CPair<String, Integer> parseReference(String str) {
        if (str == null) {
            return new CPair<>(null, 0);
        }
        int num = 0;
        while (num < str.length() && str.charAt(num) == '*') ++num;
        return new CPair<>(str.substring(num), num);
    }

    private CPair<String, Class<?>> resolveParameter(CJobParam input, Map<String, Object> valRef, Map<String, Class<?>> typeRef) {
        CPair<String, Integer> nameRefPair = parseReference(input.getName());
        CPair<String, Integer> typeRefPair = null;

        Class<?> exactlyType = null;
        // dereference 找到真正的参数名称
        String exactlyName = dereferenceOfName(valRef, nameRefPair);

        if (input.getType() instanceof String) {
            typeRefPair = parseReference((String) input.getType());
        } else if (input.getType() instanceof Class<?>) {
            exactlyType = (Class<?>) input.getType();
        } else {
            throw new CUnsupportedOperationException("can not parse type of input, " + input.getType());
        }

        // dereference 找到真正的参数类型
        if (typeRefPair != null) {
            String name = dereferenceOfName(valRef, typeRefPair);
            exactlyType = typeRef.get(name);
        }

        if (exactlyName == null || exactlyType == null) {
            throw new CJobParamNotFoundException("cannot resolve parameter of  " + input + " type or name is null :" +
                    "( type: " + exactlyType + ", name: " + exactlyName + "). ");
        }

        return new CPair<>(exactlyName, exactlyType);
    }

    private CPair<CExecutor, List<Map<String, Class<?>>>> build(CBeanFactory factory, CJobDetail job, CExecutorContext context) {
        Map<String, Class<?>> contextTypes = new HashMap<>();

        if (job.getType() == CJobDetail.TYPE_EXEC) {
            return new CPair<>(new CSimpleExecutor(context, factory.getConfig(), job.getMethod(), job.isIgnoreException(), null/*job.getAttachment()*/), null);
        } else if (job.getType() == CJobDetail.TYPE_SCHEDULE) {

            List<Map<String, Class<?>>> everyContextTypes = new ArrayList<>();

            CExecutor[] executors = new CExecutor[job.getSubJobs().length];
            CJobParam[] lastOutput = null;
            for (int i = 0; i < job.getSubJobs().length; i++) {

                //
                CJobDetail subJob = job.getSubJobs()[i];
                // set current context

//                CDefaultDefaultWritableDataSet valRef = new HashMap<>();
                Map<String, Class<?>> typeRef = new HashMap<>(contextTypes);

                if(subJob.getAttachment() != null){

                    valRef.putAll(subJob.getAttachment());
                }

                valRef.forEach((k, v) -> {
                    if (v != null && k != null) {
                        typeRef.put(k, v.getClass());
                    }
                });

                // 解析输入参数
                CJobParam[] inputs = subJob.getInputs();
                for (CJobParam input : inputs) {
                    // 解析出正在的变量名称和类型
                    CPair<String, Class<?>> parameter = resolveParameter(input, valRef, typeRef);
                    typeRef.put(parameter.getFirst(), parameter.getSecond());
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        typeRef.put(parameter.getFirst(), parameter.getSecond());
                    }
                    CPair<Boolean, List<CJobParam>> checked = hasParam(lastOutput, parameter);
                    if (i != 0 && !checked.getFirst()) {
                        throw new CJobParamNotFoundException(parameter, checked.getSecond());
                    }
                    input.setType(parameter.getSecond());
                    input.setName(parameter.getFirst());
                }
                // 解析输出参数, 这里需要合并这一次和上一次的输出
                // 因为在Removal中可能会引用这一次和上一次的输出
                CJobParam[] outputs = subJob.getOutputs();
                if(lastOutput == null){
                    lastOutput = new CJobParam[outputs.length];
                }else{
                    lastOutput = Arrays.copyOf(lastOutput,lastOutput.length + outputs.length);
                }

                for (int j = 0; j < outputs.length; ++j) {
                    CJobParam output = outputs[j];
                    CPair<String, Class<?>> parameter = resolveParameter(output, valRef, typeRef);
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        contextTypes.put(parameter.getFirst(), parameter.getSecond());
                        typeRef.put(parameter.getFirst(), parameter.getSecond());
                    }
                    lastOutput[j + lastOutput.length - outputs.length] = output;//new CJobParam(parameter.getFirst(), output.getInfo(), output.isNullable(), parameter.getSecond());
                    output.setType(parameter.getSecond());
                    output.setName(parameter.getFirst());
                }
                List<CJobParam> newOutput = new LinkedList<>(Arrays.asList(outputs));
                CJobParam[] removals = subJob.getRemovals();
                for (CJobParam removal : removals) {
                    CPair<String, Class<?>> parameter = resolveParameter(removal, valRef, typeRef);

                    CPair<Boolean, List<CJobParam>> checked = hasParam(lastOutput, parameter);
                    if (!checked.getFirst()) {
                        throw new CJobParamNotFoundException(parameter, checked.getSecond());
                    }
                    CPair<Boolean, List<CJobParam>> matchedOutput = hasParam(outputs, parameter);

                    if(matchedOutput.getFirst()){
                        newOutput.remove(matchedOutput.getSecond().get(0));
                    }

                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        contextTypes.remove(parameter.getFirst());
                        typeRef.remove(parameter.getFirst());
                    }
                    removal.setType(parameter.getSecond());
                    removal.setName(parameter.getFirst());
                }
                // set real output
                lastOutput = newOutput.toArray(new CJobParam[0]);

                executors[i] = build(factory, subJob, context).getFirst();
                everyContextTypes.add(new HashMap<>(contextTypes));
            }
            return new CPair<>(new CExecutorJob(context, factory.getConfig(), job.isIgnoreException(), executors), everyContextTypes);
        }
        throw new CUnsupportedOperationException("unsupported job type: " + job.getType());
    }

    private String dereferenceOfName(Map<String, Object> ref, CPair<String, Integer> pair) {
        int times = pair.getSecond();
        String name = pair.getFirst();
        while (times-- > 0) {
            Object obj = ref.get(name);
            if (obj == null) {
                throw new CJobParamNotFoundException("parameter not found , dereference name of: " + name + " is null. ");
            } else if (obj instanceof String) {
                name = (String) obj;
            } else {
                throw new CUnsupportedOperationException("can not dereference of " + pair + " from type which is not instanceof string, " + obj);
            }
        }
        return name;
    }

    private CPair<Boolean, List<CJobParam>> hasParam(CJobParam[] params, CPair<String, Class<?>> pair) {

        List<CJobParam> candidates = new ArrayList<>();

        if (params == null) {
            return new CPair<>(false, candidates);
        }
        for (CJobParam param : params) {
            if (pair.getSecond().isAssignableFrom((Class<?>) param.getType())
                    && pair.getFirst().equals(param.getName())) {
                candidates.clear();
                candidates.add(param);
                return new CPair<>(true, candidates);
            }
            if (pair.getSecond().isAssignableFrom((Class<?>) param.getType())
                    || pair.getFirst().equals(param.getName())) {
                candidates.add(param);
            }
        }
        return new CPair<>(false, candidates);
    }


    public CExecutor forceRun(@CAutowired CBeanFactory factory,
                              @CExecutorInput CJobDetail job,
                              @CExecutorInput CDefaultDefaultReadOnlyDataSet input) {
        CExecutor executor = toExecutor(factory, job).getFirst();
        executor.setAttachment(input);
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();
        return executor;
    }

    public CPair<CExecutor, CJobSummary> toExecutor(@CAutowired CBeanFactory factory,
                                                    @CExecutorInput CJobDetail job) {
        CJobDetail newJob = (CJobDetail) job.cloneSelf();
        CPair<CExecutor, List<Map<String, Class<?>>>> build = build(factory, newJob, new CExecutorContext());
        return new CPair<>(build.getFirst(), new CJobSummary(build.getSecond(),newJob));
    }

    public CJobDetail readJobFromJson(@CExecutorInput String json) {
        return JSON.parseObject(json, CJobDetail.class);
    }


}
