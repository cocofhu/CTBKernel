package com.cocofhu.ctb.kernel.core.exec;

import java.util.Arrays;

/**
 * @author cocofhu
 */
public class CExecutorMethod {
    private final String beanName;
    private final Class<?> clazz;
    private final String methodName;
    private final Class<?>[] parameterTypes;

    public CExecutorMethod(String beanName, Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        this.beanName = beanName;
        this.clazz = clazz;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;
    }


    public String getBeanName() {
        return beanName;
    }

    public Class<?> getBeanClass() {
        return clazz;
    }

    public String getMethodName() {
        return methodName;
    }

    public Class<?>[] getParameterTypes() {
        return parameterTypes;
    }


    @Override
    public String toString() {
        return "CExecutorMethod{" +
                "beanName='" + beanName + '\'' +
                ", clazz=" + clazz +
                ", methodName='" + methodName + '\'' +
                ", parameterTypes=" + Arrays.toString(parameterTypes) +
                '}';
    }
}
