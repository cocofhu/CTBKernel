package com.cocofhu.ctb.kernel.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author cocofhu
 */
public class CParameterWrapper implements CMateData {
    private final Parameter parameter;
    private final CTBContext context;

    private final CExecutableWrapper executor;

    public CParameterWrapper(Parameter parameter, CTBContext context, CExecutableWrapper executor) {
        this.parameter = parameter;
        this.context = context;
        this.executor = executor;
    }


    public List<CValueWrapper> resolveParameterValues(){
        return context.getValueResolver().resolveValues(this,context);
    }

    public Parameter getParameter() {
        return parameter;
    }

    public CExecutableWrapper getExecutableWrapper() {
        return executor;
    }

    @Override
    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return parameter.getAnnotation(clazz);
    }

}
