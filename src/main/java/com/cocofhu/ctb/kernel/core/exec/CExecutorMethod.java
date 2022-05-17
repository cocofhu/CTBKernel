package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.exception.job.CExecBadMethodException;
import com.cocofhu.ctb.kernel.util.CCloneable;

import java.util.Arrays;

/**
 * @author cocofhu
 */
public class CExecutorMethod implements CCloneable {
    private final String beanName;
    private final Class<?> clazz;
    private final String methodName;
    private final Class<?>[] parameterTypes;

    public CExecutorMethod(String beanName, Class<?> clazz, String methodName, Class<?>[] parameterTypes) {
        this.beanName = beanName;
        this.clazz = clazz;
        this.methodName = methodName;
        this.parameterTypes = parameterTypes;

        if(beanName == null && clazz == null){
            throw new CExecBadMethodException(this, "can not create executor method object, both name and type are null, it means can not resolve bean definition.");
        }
        if(methodName == null){
            throw new CExecBadMethodException(this, "can not create executor method object, bad method name, method name must be not null.");
        }
    }

    public CExecutorMethod(String beanName, String methodName) {
        this(beanName,null,methodName,null);
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
