package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public class CParameterWrapper {
    private Parameter parameter;
    public <A extends Annotation> A findAnnotation(Class<A> annotationType){
        return parameter.getAnnotation(annotationType);
    }
//    public Object resolveParameterValue(CBeanFactory factory){
//
//    }

    public CParameterWrapper(Parameter parameter) {
        this.parameter = parameter;
    }
}
