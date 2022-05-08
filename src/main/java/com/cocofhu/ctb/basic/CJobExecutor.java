package com.cocofhu.ctb.basic;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.basic.entity.CJobDetail;
import com.cocofhu.ctb.basic.entity.CJobParam;
import com.cocofhu.ctb.kernel.anno.CAttachmentArgs;
import com.cocofhu.ctb.kernel.anno.CAutowired;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.exec.CExecutorJob;
import com.cocofhu.ctb.kernel.core.exec.CSimpleExecutor;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.CJobParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.CUnsupportedOperationException;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CJobExecutor {


    private CTBPair<String, Integer> parseReference(String str) {
        if (str == null) {
            return new CTBPair<>(null, 0);
        }
        int num = 0;
        while (num < str.length() && str.charAt(num) == '*') ++num;
        return new CTBPair<>(str.substring(num), num);
    }

    private CTBPair<String, Class<?>> resolveParameter(CJobParam input, Map<String, Object> valRef, Map<String, Class<?>> typeRef) {
        CTBPair<String, Integer> nameRefPair = parseReference(input.getName());
        CTBPair<String, Integer> typeRefPair = null;

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

        return new CTBPair<>(exactlyName, exactlyType);
    }

    private CTBPair<CExecutor, List<Map<String, Class<?>>>> build(CBeanFactory factory, CJobDetail job, CExecutorContext context) {
        Map<String, Class<?>> contextTypes = new HashMap<>();

        if (job.getType() == CJobDetail.TYPE_EXEC) {
            return new CTBPair<>(new CSimpleExecutor(context, factory.getContext(), job.getMethod(), job.isIgnoreException(), job.getAttachment()), null);
        } else if (job.getType() == CJobDetail.TYPE_SCHEDULE) {

            List<Map<String, Class<?>>> everyContextTypes = new ArrayList<>();

            CExecutor[] executors = new CExecutor[job.getSubJobs().length];
            CJobParam[] lastOutput = null;
            for (int i = 0; i < job.getSubJobs().length; i++) {

                //
                CJobDetail subJob = job.getSubJobs()[i];
                // set current context
                Map<String, Object> valRef = new HashMap<>(subJob.getAttachment());
                Map<String, Class<?>> typeRef = new HashMap<>(contextTypes);

                valRef.forEach((k, v) -> {
                    if (v != null && k != null) {
                        typeRef.put(k, v.getClass());
                    }
                });

                // 解析输入参数
                CJobParam[] inputs = subJob.getInputs();
                for (CJobParam input : inputs) {
                    // 解析出正在的变量名称和类型
                    CTBPair<String, Class<?>> parameter = resolveParameter(input, valRef, typeRef);
                    typeRef.put(parameter.getFirst(), parameter.getSecond());
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        typeRef.put(parameter.getFirst(), parameter.getSecond());
                    }
                    CTBPair<Boolean, List<CJobParam>> checked = hasParam(lastOutput, parameter);
                    if (i != 0 && !checked.getFirst()) {
                        throw new CJobParamNotFoundException(parameter, checked.getSecond());
                    }
                    input.setType(parameter.getSecond());
                    input.setName(parameter.getFirst());
                }
                // 解析输出参数
                CJobParam[] outputs = subJob.getOutputs();
                lastOutput = new CJobParam[outputs.length];
                for (int j = 0; j < outputs.length; ++j) {
                    CJobParam output = outputs[j];
                    CTBPair<String, Class<?>> parameter = resolveParameter(output, valRef, typeRef);
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        contextTypes.put(parameter.getFirst(), parameter.getSecond());
                        typeRef.put(parameter.getFirst(), parameter.getSecond());
                    }
                    lastOutput[j] = new CJobParam(parameter.getFirst(), output.getInfo(), output.isNullable(), parameter.getSecond());
                    output.setType(parameter.getSecond());
                    output.setName(parameter.getFirst());
                }

                CJobParam[] removals = subJob.getRemovals();
                for (CJobParam removal : removals) {
                    CTBPair<String, Class<?>> parameter = resolveParameter(removal, valRef, typeRef);
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        contextTypes.remove(parameter.getFirst());
                        typeRef.remove(parameter.getFirst());
                    }
                    removal.setType(parameter.getSecond());
                    removal.setName(parameter.getFirst());
                }
                executors[i] = build(factory, subJob, context).getFirst();
                everyContextTypes.add(new HashMap<>(contextTypes));
            }
            return new CTBPair<>(new CExecutorJob(context, factory.getContext(), job.isIgnoreException(), executors), everyContextTypes);
        }
        throw new CUnsupportedOperationException("unsupported job type: " + job.getType());
    }

    private String dereferenceOfName(Map<String, Object> ref, CTBPair<String, Integer> pair) {
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

    private CTBPair<Boolean, List<CJobParam>> hasParam(CJobParam[] params, CTBPair<String, Class<?>> pair) {

        List<CJobParam> candidates = new ArrayList<>();

        if (params == null) {
            return new CTBPair<>(false, candidates);
        }
        for (CJobParam param : params) {
            if (pair.getSecond().isAssignableFrom((Class<?>) param.getType())
                    && pair.getFirst().equals(param.getName())) {
                return new CTBPair<>(true, candidates);
            }
            if (pair.getSecond().isAssignableFrom((Class<?>) param.getType())
                    || pair.getFirst().equals(param.getName())) {
                candidates.add(param);
            }
        }
        return new CTBPair<>(false, candidates);
    }


    public CExecutor forceRun(@CAutowired CBeanFactory factory,
                              @CAttachmentArgs CJobDetail job,
                              @CAttachmentArgs Map<String, Object> input) {
        CExecutor executor = toExecutor(factory, job).getFirst();
        executor.setAttachment(input);
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();
        return executor;
    }

    public CTBPair<CExecutor, CTBPair<List<Map<String, Class<?>>>, CJobDetail>> toExecutor(@CAutowired CBeanFactory factory,
                                                                                           @CAttachmentArgs CJobDetail job) {
        CJobDetail newJob = (CJobDetail) job.cloneSelf();
        CTBPair<CExecutor, List<Map<String, Class<?>>>> build = build(factory, newJob, new CExecutorContext());
        return new CTBPair<>(build.getFirst(), new CTBPair<>(build.getSecond(), newJob));
    }

    public CJobDetail readJobFromJson(@CAttachmentArgs String json) {
        return JSON.parseObject(json, CJobDetail.class);
    }


}
