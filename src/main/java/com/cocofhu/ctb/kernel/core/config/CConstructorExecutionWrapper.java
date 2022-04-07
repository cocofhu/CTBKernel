package com.cocofhu.ctb.kernel.core.config;

import java.lang.reflect.Constructor;

public class CConstructorExecutionWrapper {

    private final Constructor<?> constructor;
    private final Object[] parameters;

    public CConstructorExecutionWrapper(Constructor<?> constructor, Object[] parameters) {
        this.constructor = constructor;
        this.parameters = parameters;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Object[] getParameters() {
        return parameters;
    }
}

