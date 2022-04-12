package com.cocofhu.ctb.kernel.core.config;


import java.lang.annotation.Annotation;
import java.lang.reflect.Constructor;
import java.lang.reflect.Parameter;


public class CConstructorWrapper {

    private final Constructor<?> constructor;



    public <A extends Annotation> A findAnnotation(Class<A> annotationType){
        return constructor.getAnnotation(annotationType);
    }

    public CParameterWrapper[] listParameters(){
        Parameter[] parameters = constructor.getParameters();
        CParameterWrapper[] result = new CParameterWrapper[parameters.length];
        for (int i = 0; i < parameters.length; i++) {
            result[i] = new CParameterWrapper(parameters[i]);
        }
        return result;
    }

    public CConstructorWrapper(Constructor<?> constructor, Object[] parameters) {
        this.constructor = constructor;
//        this.parameters = parameters;
    }

    public Constructor<?> getConstructor() {
        return constructor;
    }

    public Object[] getParameters() {
        return null;
    }
}

