package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.exec.CExecBadInfoException;
import com.cocofhu.ctb.kernel.exception.exec.CExecConflictParameterException;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.CReflectionUtils;
import com.cocofhu.ctb.kernel.util.ds.*;

import java.lang.annotation.Annotation;
import java.util.*;

public class CExecutorUtils {


    public static CExecutorDefinition toExecDetail(CBeanFactory factory, CExecutorMethod executorMethod) {


        //
        CBeanDefinition beanDefinition = factory.getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
        CExecutableWrapper method = new CExecutableWrapper(
                CReflectionUtils.findMethod(beanDefinition.getBeanClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes()),
                factory.getConfig(), beanDefinition, null);

        CExecutableWrapper ctor = factory.getConfig().getInstanceCreator().resolveConstructor(beanDefinition, factory.getConfig(), null);
        CTripe<Set<CParameterDefinition>, Set<CParameterDefinition>, Set<CParameterDefinition>> ctorParams = resolveIORFromExecutable(ctor);
        CTripe<Set<CParameterDefinition>, Set<CParameterDefinition>, Set<CParameterDefinition>> methodParams = resolveIORFromExecutable(method);

        // 由于后续操作会改变Input Output的完整性
        // 这里先要检查每个单独的Input Output Removal
        checkParamValidAndThrow(ctorParams.getFirst().toArray(new CParameterDefinition[0]), "ctor inputs", "0");
        checkParamValidAndThrow(ctorParams.getSecond().toArray(new CParameterDefinition[0]), "ctor outputs", "0");
        checkParamValidAndThrow(ctorParams.getThird().toArray(new CParameterDefinition[0]), "ctor removals", "0");

        checkParamValidAndThrow(methodParams.getFirst().toArray(new CParameterDefinition[0]), "method inputs", "0");
        checkParamValidAndThrow(methodParams.getSecond().toArray(new CParameterDefinition[0]), "method outputs", "0");
        checkParamValidAndThrow(methodParams.getThird().toArray(new CParameterDefinition[0]), "method removals", "0");


        Set<CParameterDefinition> ctorOutputs = ctorParams.getSecond();
        Set<CParameterDefinition> methodInputs = methodParams.getFirst();
        Set<CParameterDefinition> actualInputs = ctorParams.getFirst();
        // 1、构造函数的Outputs - 构造函数的Removals 得到实际的构造函数输出
        ctorOutputs.removeAll(ctorParams.getThird());
        // 2、方法的Inputs - 构造函数的实际输出，得到方法的实际输入
        methodInputs.removeAll(ctorOutputs);
        // 3、构造函数的输入加上方法的实际输入，得到实际输入
        actualInputs.addAll(methodInputs);

        // 检查最终Inputs参数
        checkParamValidAndThrow(actualInputs.toArray(new CParameterDefinition[0]), "method inputs", "0");


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

        if (basicInfo.getFirst() == null || basicInfo.getSecond() == null || basicInfo.getThird().getFirst() == null || basicInfo.getThird().getSecond() == null) {
            throw new CExecBadInfoException(basicInfo.getFirst(), basicInfo.getSecond(), basicInfo.getThird().getFirst());
        }

        return new CExecutorDefinition(basicInfo.getFirst(), basicInfo.getSecond(), basicInfo.getThird().getFirst(),
                actualInputs.toArray(new CParameterDefinition[0]),
                methodParams.getSecond().toArray(new CParameterDefinition[0]),
                methodParams.getThird().toArray(new CParameterDefinition[0]),
                basicInfo.getThird().getSecond(),
                executorMethod,
                null,
                null
        );


    }


    static CPair<Boolean, List<CParameterDefinition>> checkParamValid(CParameterDefinition[] params) {
        if (params == null) {
            params = new CParameterDefinition[0];
        }
        List<CParameterDefinition> conflictParams = new ArrayList<>();
        HashMap<String, CParameterDefinition> has = new HashMap<>();
        for (CParameterDefinition param : params) {
            CParameterDefinition obj = has.get(param.getName());
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

    static void checkParamValidAndThrow(CParameterDefinition[] params, String which, String layer) {
        CPair<Boolean, List<CParameterDefinition>> pair = checkParamValid(params);
        if (!pair.getFirst()) {
            throw new CExecConflictParameterException(params, "conflict " + which + " parameters at layer " + layer + " , " + Arrays.toString(pair.getSecond().stream().map(
                    p -> "( type: " + p.getType() + ", name: " + p.getName() + ")"
            ).toArray(String[]::new)));
        }
    }


    private static CTripe<Set<CParameterDefinition>, Set<CParameterDefinition>, Set<CParameterDefinition>> resolveIORFromExecutable(CExecutableWrapper executableWrapper) {

        Set<CParameterDefinition> inputs = new HashSet<>();
        Set<CParameterDefinition> outputs = new HashSet<>();
        Set<CParameterDefinition> removals = new HashSet<>();
        CParameterWrapper[] parameters = executableWrapper.acquireParameterWrappers();

        for (CParameterWrapper parameter : parameters) {
            CExecutorInput annotation = parameter.getAnnotation(CExecutorInput.class);
            if (annotation != null) {
                String name = annotation.name();
                if (CStringUtils.isEmpty(name)) {
                    name = parameter.getParameter().getName();
                }
                inputs.add(new CParameterDefinition(name, annotation.info(), parameter.getParameter().getType()));
            }
        }

        resolveIORFromAnnotation(executableWrapper, inputs, CExecutorContextInputs.class);
        resolveIORFromAnnotation(executableWrapper, inputs, CExecutorContextRawInputs.class);


        resolveIORFromAnnotation(executableWrapper, outputs, CExecutorOutputs.class);
        resolveIORFromAnnotation(executableWrapper, outputs, CExecutorRawOutputs.class);

        resolveIORFromAnnotation(executableWrapper, removals, CExecutorRemovals.class);
        resolveIORFromAnnotation(executableWrapper, removals, CExecutorRawRemovals.class);

        resolveIORFromAnnotation(executableWrapper, inputs, CExecutorContextInput.class);
        resolveIORFromAnnotation(executableWrapper, inputs, CExecutorContextRawInput.class);

        resolveIORFromAnnotation(executableWrapper, outputs, CExecutorOutput.class);
        resolveIORFromAnnotation(executableWrapper, outputs, CExecutorRawOutput.class);

        resolveIORFromAnnotation(executableWrapper, removals, CExecutorRemoval.class);
        resolveIORFromAnnotation(executableWrapper, removals, CExecutorRawRemoval.class);

        return new CTripe<>(inputs, outputs, removals);
    }

    private static void resolveIORFromAnnotation(CExecutableWrapper executableWrapper, Set<CParameterDefinition> list, Class<? extends Annotation> clazz) {
        Annotation annotation = executableWrapper.getAnnotation(clazz);
        if (annotation instanceof CExecutorContextInputs) {
            CExecutorContextInputs casted = (CExecutorContextInputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CParameterDefinition(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorContextRawInputs) {
            CExecutorContextRawInputs casted = (CExecutorContextRawInputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CParameterDefinition(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorOutputs) {
            CExecutorOutputs casted = (CExecutorOutputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CParameterDefinition(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRawOutputs) {
            CExecutorRawOutputs casted = (CExecutorRawOutputs) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CParameterDefinition(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRemovals) {
            CExecutorRemovals casted = (CExecutorRemovals) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CParameterDefinition(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorRawRemovals) {
            CExecutorRawRemovals casted = (CExecutorRawRemovals) annotation;
            Arrays.stream(casted.value()).forEach(a -> list.add(new CParameterDefinition(a.name(), a.info(), a.type())));
        } else if (annotation instanceof CExecutorContextInput) {
            CExecutorContextInput casted = (CExecutorContextInput) annotation;
            list.add(new CParameterDefinition(casted.name(), casted.info(), casted.type()));
        } else if (annotation instanceof CExecutorContextRawInput) {
            CExecutorContextRawInput casted = (CExecutorContextRawInput) annotation;
            list.add(new CParameterDefinition(casted.name(), casted.info(), casted.type()));
        } else if (annotation instanceof CExecutorOutput) {
            CExecutorOutput casted = (CExecutorOutput) annotation;
            list.add(new CParameterDefinition(casted.name(), casted.info(), casted.type()));
        } else if (annotation instanceof CExecutorRawOutput) {
            CExecutorRawOutput casted = (CExecutorRawOutput) annotation;
            list.add(new CParameterDefinition(casted.name(), casted.info(), casted.type()));
        } else if (annotation instanceof CExecutorRemoval) {
            CExecutorRemoval casted = (CExecutorRemoval) annotation;
            list.add(new CParameterDefinition(casted.name(), casted.info(), casted.type()));
        } else if (annotation instanceof CExecutorRawRemoval) {
            CExecutorRawRemoval casted = (CExecutorRawRemoval) annotation;
            list.add(new CParameterDefinition(casted.name(), casted.info(), casted.type()));
        }
    }


}
