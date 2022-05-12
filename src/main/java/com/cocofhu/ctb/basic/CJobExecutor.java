package com.cocofhu.ctb.basic;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobDetail;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobParam;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobSummary;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.job.CJobParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.job.CJobUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.ReflectionUtils;

import java.lang.annotation.Annotation;
import java.util.*;

public class CJobExecutor {


    public CExecutor forceRun(@CAutowired CBeanFactory factory,
                              @CExecutorInput CJobDetail job,
                              @CExecutorInput CDefaultDefaultReadOnlyDataSet<String, Object> input) {
        CExecutor executor = toExecutor(factory, job);
        executor.setAttachment(input);
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();
        return executor;
    }

    public CExecutor toExecutor(@CAutowired CBeanFactory factory,
                                @CExecutorInput CJobDetail job) {
        CJobDetail newJob = (CJobDetail) job.cloneSelf();
        CPair<CExecutor, List<Map<String, Class<?>>>> build = build(factory, newJob, new CExecutorContext());
        return build.getFirst();
    }

    public CJobDetail toJobDetail(CBeanFactory factory, CExecutorMethod executorMethod) {

        //
        CBeanDefinition beanDefinition = factory.getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
        CExecutableWrapper method = new CExecutableWrapper(
                ReflectionUtils.findMethod(beanDefinition.getBeanClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes()),
                factory.getConfig(), beanDefinition, null);
        CExecutableWrapper ctor = factory.getConfig().getInstanceCreator().resolveConstructor(beanDefinition, factory.getConfig(), null);
        CTripe<Set<CJobParam>, Set<CJobParam>, Set<CJobParam>> ctorParams = resolveIORFromExecutable(ctor);
        CTripe<Set<CJobParam>, Set<CJobParam>, Set<CJobParam>> methodParams = resolveIORFromExecutable(method);

        // 由于后续操作会改变Input Output的完整性
        // 这里先要检查每个单独的Input Output Removal
        // check




        Set<CJobParam> ctorOutputs = ctorParams.getSecond();
        Set<CJobParam> methodInputs = methodParams.getFirst();
        Set<CJobParam> actualInputs = ctorParams.getFirst();



        System.out.println(methodInputs);

        ctorOutputs.removeAll(ctorParams.getThird());
        System.out.println(ctorOutputs);
        methodInputs.removeAll(ctorOutputs);
        System.out.println("1");
        System.out.println(methodInputs);
        actualInputs.addAll(methodInputs);

        System.out.println(methodInputs);
        System.out.println(actualInputs);

        // check input and output remove is invalid
        CPair<Boolean, List<CJobParam>> inputsCheckResult = checkParamValid(actualInputs.toArray(new CJobParam[0]));
//        CPair<Boolean, List<CJobParam>> inputsCheckResult = checkParamValid(actualInputs.toArray(new CJobParam[0]));
        System.out.println(inputsCheckResult);


        CJob infoOfJob = method.acquireNearAnnotation(CJob.class);

        return new CJobDetail(infoOfJob.name(), infoOfJob.info(), infoOfJob.group(),
                actualInputs.toArray(new CJobParam[0]),
                methodParams.getSecond().toArray(new CJobParam[0]),
                methodParams.getThird().toArray(new CJobParam[0]),
                infoOfJob.ignoreException(),
                executorMethod,
                null,
                null
        );


    }

    private CPair<Boolean,List<CJobParam>> checkParamValid(CJobParam[] params){
        List<CJobParam> conflictParams = new ArrayList<>();
        HashMap<String,CJobParam> has = new HashMap<>();
        for (CJobParam param : params) {
            CJobParam obj = has.get(param.getName());
            if (obj != null){
                if(conflictParams.size() == 0){
                    conflictParams.add(obj);
                }
                conflictParams.add(param);
            }
            has.put(param.getName(),param);
        }
        return new CPair<>(conflictParams.size()>0,conflictParams);
    }


    public CJobSummary toSummary(@CAutowired CBeanFactory factory,
                                 @CExecutorInput CJobDetail job) {
        CJobDetail newJob = (CJobDetail) job.cloneSelf();
        CPair<CExecutor, List<Map<String, Class<?>>>> build = build(factory, newJob, new CExecutorContext());
        return new CJobSummary(build.getSecond(), newJob);
    }

    public CJobDetail readJobFromJson(@CExecutorInput String json) {
        return JSON.parseObject(json, CJobDetail.class);
    }

    private CTripe<Set<CJobParam>, Set<CJobParam>, Set<CJobParam>> resolveIORFromExecutable(CExecutableWrapper executableWrapper) {

        Set<CJobParam> inputs = new HashSet<>();
        Set<CJobParam> outputs = new HashSet<>();
        Set<CJobParam> removals = new HashSet<>();
        CParameterWrapper[] parameters = executableWrapper.acquireParameterWrappers();

        for (CParameterWrapper parameter : parameters) {
            CExecutorInput annotation = parameter.getAnnotation(CExecutorInput.class);
            if (annotation != null) {
                String name = annotation.name();
                if (CStringUtils.isEmpty(name)) {
                    name = parameter.getParameter().getName();
                }
                inputs.add(new CJobParam(name, annotation.info(), parameter.getParameter().getType()));
            }
        }

        resolveIORFromAnnotation(executableWrapper, inputs, CExecutorContextInputs.class);
        resolveIORFromAnnotation(executableWrapper, inputs, CExecutorContextRawInputs.class);

        resolveIORFromAnnotation(executableWrapper, outputs, CExecutorOutputs.class);
        resolveIORFromAnnotation(executableWrapper, outputs, CExecutorRawOutputs.class);

        resolveIORFromAnnotation(executableWrapper, removals, CExecutorRemovals.class);
        resolveIORFromAnnotation(executableWrapper, removals, CExecutorRawRemovals.class);

        return new CTripe<>(inputs, outputs, removals);
    }

    private void resolveIORFromAnnotation(CExecutableWrapper executableWrapper, Set<CJobParam> list, Class<? extends Annotation> clazz) {
        Annotation annotation = executableWrapper.getAnnotation(clazz);
        if (annotation instanceof CExecutorContextInputs) {
            CExecutorContextInputs casted = (CExecutorContextInputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CJobParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorContextRawInputs) {
            CExecutorContextRawInputs casted = (CExecutorContextRawInputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CJobParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorOutputs) {
            CExecutorOutputs casted = (CExecutorOutputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CJobParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRawOutputs) {
            CExecutorRawOutputs casted = (CExecutorRawOutputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CJobParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRemovals) {
            CExecutorRemovals casted = (CExecutorRemovals) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CJobParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRawRemovals) {
            CExecutorRawRemovals casted = (CExecutorRawRemovals) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CJobParam(a.name(), a.info(), a.type())));
        } /*else {
            // throw new CUnsupportedOperationException("never reach...");
            // Can not reach here
        }*/

    }


    private CPair<String, Integer> parseReference(String str) {
        if (str == null) {
            return new CPair<>(null, 0);
        }
        int num = 0;
        while (num < str.length() && str.charAt(num) == '*') ++num;
        return new CPair<>(str.substring(num), num);
    }

    private CPair<String, Class<?>> resolveParameter(CJobParam input, CDefaultLayerDataSet<String, Object> valRef, CDefaultLayerDataSet<String, Class<?>> typeRef) {
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
            throw new CJobUnsupportedOperationException("can not parse type of input, " + input.getType());
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

        CDefaultLayerDataSet<String, Class<?>> contextTypes = new CDefaultLayerDataSet<>();
        if (job.getType() == CJobDetail.TYPE_EXEC) {
            return new CPair<>(new CSimpleExecutor(context, factory.getConfig(), job.getMethod(), job.isIgnoreException(), job.getAttachment()), null);
        } else if (job.getType() == CJobDetail.TYPE_SCHEDULE) {
            List<Map<String, Class<?>>> everyContextTypes = new ArrayList<>();
            CExecutor[] executors = new CExecutor[job.getSubJobs().length];
            CJobParam[] lastOutput = null;
            for (int i = 0; i < job.getSubJobs().length; i++) {

                CJobDetail subJob = job.getSubJobs()[i];
                // set current context

                CDefaultLayerDataSet<String, Object> valRef = new CDefaultLayerDataSet<>();
                CDefaultLayerDataSet<String, Class<?>> typeRef = contextTypes.newLayer();

                if (subJob.getAttachment() != null) {
                    valRef.putAll(subJob.getAttachment());
                }

                valRef.entries().forEach(e -> {
                    if (e.getKey() != null && e.getValue() != null) {
                        typeRef.put(e.getKey(), e.getValue().getClass());
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
                if (lastOutput == null) {
                    lastOutput = new CJobParam[outputs.length];
                } else {
                    lastOutput = Arrays.copyOf(lastOutput, lastOutput.length + outputs.length);
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

                    if (matchedOutput.getFirst()) {
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
                everyContextTypes.add(contextTypes.toMap());
            }
            return new CPair<>(new CExecutorJob(context, factory.getConfig(), job.isIgnoreException(), executors), everyContextTypes);
        }
        throw new CJobUnsupportedOperationException("unsupported job type: " + job.getType());
    }

    private String dereferenceOfName(CDefaultLayerDataSet<String, Object> ref, CPair<String, Integer> pair) {
        int times = pair.getSecond();
        String name = pair.getFirst();
        while (times-- > 0) {
            Object obj = ref.get(name);
            if (obj == null) {
                throw new CJobParamNotFoundException("parameter not found , dereference name of: " + name + " is null. ");
            } else if (obj instanceof String) {
                name = (String) obj;
            } else {
                throw new CJobUnsupportedOperationException("can not dereference of " + pair + " from type which is not instanceof string, " + obj);
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


}
