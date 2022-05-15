package com.cocofhu.ctb.basic;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecDetail;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecParam;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecSummary;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.job.CExecConflictParameterException;
import com.cocofhu.ctb.kernel.exception.job.CExecParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.job.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.ReflectionUtils;
import com.cocofhu.ctb.kernel.util.ds.*;

import java.lang.annotation.Annotation;
import java.util.*;

public class CExecBuilder {


    public CExecutor forceRun(@CAutowired CBeanFactory factory,
                              @CExecutorInput CExecDetail job,
                              @CExecutorInput CDefaultDefaultReadOnlyDataSet<String, Object> input) {
        CExecutor executor = toExecutor(factory, job);
        executor.setAttachment(input);
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();
        return executor;
    }

    public CExecutor toExecutor(@CAutowired CBeanFactory factory,
                                @CExecutorInput CExecDetail job) {
        CExecDetail newJob = (CExecDetail) job.cloneSelf();
        CPair<CExecutor, List<Map<String, Class<?>>>> build = build(factory, newJob, new CExecutorContext());
        return build.getFirst();
    }

    public CExecDetail toJobDetail(CBeanFactory factory, CExecutorMethod executorMethod) {


        //
        CBeanDefinition beanDefinition = factory.getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
        CExecutableWrapper method = new CExecutableWrapper(
                ReflectionUtils.findMethod(beanDefinition.getBeanClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes()),
                factory.getConfig(), beanDefinition, null);

        CExecutableWrapper ctor = factory.getConfig().getInstanceCreator().resolveConstructor(beanDefinition, factory.getConfig(), null);
        CTripe<Set<CExecParam>, Set<CExecParam>, Set<CExecParam>> ctorParams = resolveIORFromExecutable(ctor);
        CTripe<Set<CExecParam>, Set<CExecParam>, Set<CExecParam>> methodParams = resolveIORFromExecutable(method);

        // 由于后续操作会改变Input Output的完整性
        // 这里先要检查每个单独的Input Output Removal
        checkParamValidAndThrow(ctorParams.getFirst().toArray(new CExecParam[0]), "ctor inputs");
        checkParamValidAndThrow(ctorParams.getSecond().toArray(new CExecParam[0]), "ctor outputs");
        checkParamValidAndThrow(ctorParams.getThird().toArray(new CExecParam[0]), "ctor removals");

        checkParamValidAndThrow(methodParams.getFirst().toArray(new CExecParam[0]), "method inputs");
        checkParamValidAndThrow(methodParams.getSecond().toArray(new CExecParam[0]), "method outputs");
        checkParamValidAndThrow(methodParams.getThird().toArray(new CExecParam[0]), "method removals");


        Set<CExecParam> ctorOutputs = ctorParams.getSecond();
        Set<CExecParam> methodInputs = methodParams.getFirst();
        Set<CExecParam> actualInputs = ctorParams.getFirst();
        // 1、构造函数的Outputs - 构造函数的Removals 得到实际的构造函数输出
        ctorOutputs.removeAll(ctorParams.getThird());
        // 2、方法的Inputs - 构造函数的实际输出，得到方法的实际输入
        methodInputs.removeAll(ctorOutputs);
        // 3、构造函数的输入加上方法的实际输入，得到实际输入
        actualInputs.addAll(methodInputs);

        // 检查最终Inputs参数
        checkParamValidAndThrow(actualInputs.toArray(new CExecParam[0]), "method inputs");


        // 这里没有必要再弄个类出来了，后续变更是再做修改
        CWritableTripe<String, String, CWritablePair<String, Boolean>> basicInfo = new CWritableTripe<>(null, null, new CWritablePair<>(null, null));

        method.annotationForEachRecursively(CExecBasicInfo.class, anno -> {
            if (!CStringUtils.isEmpty(anno.name()) && basicInfo.getFirst() == null) {
                basicInfo.setFirst(anno.name());
            }
            if (!CStringUtils.isEmpty(anno.info()) && basicInfo.getSecond() == null) {
                basicInfo.setSecond(anno.info());
            }
            if (!CStringUtils.isEmpty(anno.group()) && basicInfo.getThird().getFirst() == null) {
                basicInfo.getThird().setFirst(anno.group());
            }
            if (basicInfo.getThird().getSecond() == null) {
                basicInfo.getThird().setSecond(anno.ignoreException());
            }
            return basicInfo.getFirst() == null || basicInfo.getSecond() == null || basicInfo.getThird().getFirst() == null || basicInfo.getThird().getSecond() == null;
        });


        return new CExecDetail(basicInfo.getFirst(), basicInfo.getSecond(), basicInfo.getThird().getFirst(),
                actualInputs.toArray(new CExecParam[0]),
                methodParams.getSecond().toArray(new CExecParam[0]),
                methodParams.getThird().toArray(new CExecParam[0]),
                basicInfo.getThird().getSecond(),
                executorMethod,
                null,
                null
        );


    }

    public CExecSummary toSummary(@CAutowired CBeanFactory factory,
                                  @CExecutorInput CExecDetail job) {
        CExecDetail newJob = (CExecDetail) job.cloneSelf();
        CPair<CExecutor, List<Map<String, Class<?>>>> build = build(factory, newJob, new CExecutorContext());
        return new CExecSummary(build.getSecond(), newJob);
    }

    public CExecDetail readJobFromJson(@CExecutorInput String json) {
        return JSON.parseObject(json, CExecDetail.class);
    }

    private CPair<Boolean, List<CExecParam>> checkParamValid(CExecParam[] params) {
        List<CExecParam> conflictParams = new ArrayList<>();
        HashMap<String, CExecParam> has = new HashMap<>();
        for (CExecParam param : params) {
            CExecParam obj = has.get(param.getName());
            if (obj != null) {
                if (conflictParams.size() == 0) {
                    conflictParams.add(obj);
                }
                conflictParams.add(param);
            }
            has.put(param.getName(), param);
        }
        return new CPair<>(conflictParams.size() == 0, conflictParams);
    }

    private void checkParamValidAndThrow(CExecParam[] params, String which) {
        CPair<Boolean, List<CExecParam>> pair = checkParamValid(params);
        if (!pair.getFirst()) {
            throw new CExecConflictParameterException(params, "conflict " + which + " parameters, " + Arrays.toString(pair.getSecond().stream().map(
                    p -> "( type: " + p.getType() + ", name: " + p.getName() + ")"
            ).toArray(String[]::new)));
        }
    }

    private CTripe<Set<CExecParam>, Set<CExecParam>, Set<CExecParam>> resolveIORFromExecutable(CExecutableWrapper executableWrapper) {

        Set<CExecParam> inputs = new HashSet<>();
        Set<CExecParam> outputs = new HashSet<>();
        Set<CExecParam> removals = new HashSet<>();
        CParameterWrapper[] parameters = executableWrapper.acquireParameterWrappers();

        for (CParameterWrapper parameter : parameters) {
            CExecutorInput annotation = parameter.getAnnotation(CExecutorInput.class);
            if (annotation != null) {
                String name = annotation.name();
                if (CStringUtils.isEmpty(name)) {
                    name = parameter.getParameter().getName();
                }
                inputs.add(new CExecParam(name, annotation.info(), parameter.getParameter().getType()));
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

    private void resolveIORFromAnnotation(CExecutableWrapper executableWrapper, Set<CExecParam> list, Class<? extends Annotation> clazz) {
        Annotation annotation = executableWrapper.getAnnotation(clazz);
        if (annotation instanceof CExecutorContextInputs) {
            CExecutorContextInputs casted = (CExecutorContextInputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CExecParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorContextRawInputs) {
            CExecutorContextRawInputs casted = (CExecutorContextRawInputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CExecParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorOutputs) {
            CExecutorOutputs casted = (CExecutorOutputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CExecParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRawOutputs) {
            CExecutorRawOutputs casted = (CExecutorRawOutputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CExecParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRemovals) {
            CExecutorRemovals casted = (CExecutorRemovals) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CExecParam(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRawRemovals) {
            CExecutorRawRemovals casted = (CExecutorRawRemovals) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CExecParam(a.name(), a.info(), a.type())));
        }
    }


    private CPair<String, Integer> parseReference(String str) {
        if (str == null) {
            return new CPair<>(null, 0);
        }
        int num = 0;
        while (num < str.length() && str.charAt(num) == '*') ++num;
        return new CPair<>(str.substring(num), num);
    }

    private CPair<String, Class<?>> resolveParameter(CExecParam input, CDefaultLayerDataSet<String, Object> valRef, CDefaultLayerDataSet<String, Class<?>> typeRef) {
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
            throw new CExecUnsupportedOperationException("can not parse type of input, " + input.getType());
        }
        // dereference 找到真正的参数类型
        if (typeRefPair != null) {
            String name = dereferenceOfName(valRef, typeRefPair);
            exactlyType = typeRef.get(name);
        }
        if (exactlyName == null || exactlyType == null) {
            throw new CExecParamNotFoundException("cannot resolve parameter of  " + input + " type or name is null :" +
                    "( type: " + exactlyType + ", name: " + exactlyName + "). ");
        }
        return new CPair<>(exactlyName, exactlyType);
    }


    // 这里还是要用递归，看能不能用个责任链来处理不同的Type
    private CPair<CExecutor, List<Map<String, Class<?>>>> build(CBeanFactory factory, CExecDetail job, CExecutorContext context) {


        if (job.getType() == CExecDetail.TYPE_EXEC) {

            return new CPair<>(new CSimpleExecutor(context, factory.getConfig(), job.getMethod(), job.isIgnoreException(), job.getAttachment()), null);
        } else if (job.getType() == CExecDetail.TYPE_SCHEDULE) {
            CDefaultLayerDataSet<String, Class<?>> contextTypes = new CDefaultLayerDataSet<>();
            List<Map<String, Class<?>>> everyContextTypes = new ArrayList<>();
            CExecutor[] executors = new CExecutor[job.getSubJobs().length];
            CExecParam[] lastOutput = null;
            for (int i = 0; i < job.getSubJobs().length; i++) {
                CExecDetail subJob = job.getSubJobs()[i];
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
                CExecParam[] inputs = subJob.getInputs();
                for (CExecParam input : inputs) {
                    // 解析出正在的变量名称和类型
                    CPair<String, Class<?>> parameter = resolveParameter(input, valRef, typeRef);
                    typeRef.put(parameter.getFirst(), parameter.getSecond());
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        typeRef.put(parameter.getFirst(), parameter.getSecond());
                    }
                    CPair<Boolean, List<CExecParam>> checked = hasParam(lastOutput, parameter);
                    if (i != 0 && !checked.getFirst()) {
                        throw new CExecParamNotFoundException(parameter, checked.getSecond());
                    }
                    input.setType(parameter.getSecond());
                    input.setName(parameter.getFirst());
                }
                // 解析输出参数, 这里需要合并这一次和上一次的输出
                // 因为在Removal中可能会引用这一次和上一次的输出
                CExecParam[] outputs = subJob.getOutputs();
                if (lastOutput == null) {
                    lastOutput = new CExecParam[outputs.length];
                } else {
                    lastOutput = Arrays.copyOf(lastOutput, lastOutput.length + outputs.length);
                }

                for (int j = 0; j < outputs.length; ++j) {
                    CExecParam output = outputs[j];
                    CPair<String, Class<?>> parameter = resolveParameter(output, valRef, typeRef);
                    if (parameter.getFirst() != null && parameter.getSecond() != null) {
                        contextTypes.put(parameter.getFirst(), parameter.getSecond());
                        typeRef.put(parameter.getFirst(), parameter.getSecond());
                    }
                    lastOutput[j + lastOutput.length - outputs.length] = output;
                    output.setType(parameter.getSecond());
                    output.setName(parameter.getFirst());
                }
                List<CExecParam> newOutput = new LinkedList<>(Arrays.asList(outputs));
                CExecParam[] removals = subJob.getRemovals();
                for (CExecParam removal : removals) {
                    CPair<String, Class<?>> parameter = resolveParameter(removal, valRef, typeRef);

                    CPair<Boolean, List<CExecParam>> checked = hasParam(lastOutput, parameter);
                    if (!checked.getFirst()) {
                        throw new CExecParamNotFoundException(parameter, checked.getSecond());
                    }
                    CPair<Boolean, List<CExecParam>> matchedOutput = hasParam(outputs, parameter);

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
                lastOutput = newOutput.toArray(new CExecParam[0]);
                executors[i] = build(factory, subJob, context).getFirst();
                everyContextTypes.add(contextTypes.toMap());
            }
            return new CPair<>(new CExecutorJob(context, factory.getConfig(), job.isIgnoreException(), executors), everyContextTypes);
        }
        throw new CExecUnsupportedOperationException("unsupported job type: " + job.getType());
    }

    private String dereferenceOfName(CDefaultLayerDataSet<String, Object> ref, CPair<String, Integer> pair) {
        int times = pair.getSecond();
        String name = pair.getFirst();
        while (times-- > 0) {
            Object obj = ref.get(name);
            if (obj == null) {
                throw new CExecParamNotFoundException("parameter not found , dereference name of: " + name + " is null. ");
            } else if (obj instanceof String) {
                name = (String) obj;
            } else {
                throw new CExecUnsupportedOperationException("can not dereference of " + pair + " from type which is not instanceof string, " + obj);
            }
        }
        return name;
    }

    private CPair<Boolean, List<CExecParam>> hasParam(CExecParam[] params, CPair<String, Class<?>> pair) {

        List<CExecParam> candidates = new ArrayList<>();

        if (params == null) {
            return new CPair<>(false, candidates);
        }
        for (CExecParam param : params) {
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
