package com.cocofhu.ctb.kernel.core.config;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.List;

/**
 * @author cocofhu
 */
public class CParameterWrapper implements CMateData {
    private final Parameter parameter;
    private final CConfig config;

    private final CExecutableWrapper executor;

    private final CReadOnlyDataSet<String, Object> dataSet;

    public CParameterWrapper(Parameter parameter, CConfig config, CExecutableWrapper executor, CReadOnlyDataSet<String, Object> dataSet) {
        this.parameter = parameter;
        this.config = config;
        this.executor = executor;
        this.dataSet = dataSet;
    }


    public List<CValueWrapper> resolveParameterValues(){
        return config.getValueResolver().resolveValues(this, config, dataSet);
    }

    public Parameter getParameter() {
        return parameter;
    }

    @Override
    public Annotation[] getAnnotations() {
        return parameter.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return parameter.getAnnotation(clazz);
    }

    @Override
    public CMateData getParent() {
        return executor;
    }

}
