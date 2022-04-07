package com.cocofhu.ctb.kernel.core.config;

import java.lang.reflect.Constructor;

public class CConstructorWrapper {

    private final Constructor<?> constructor;
    private final Object[] parameters;

    public CConstructorWrapper(Constructor<?> constructor, Object[] parameters) {
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

