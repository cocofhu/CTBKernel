package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.exception.bean.CEmptyConfigException;
import com.cocofhu.ctb.kernel.exception.bean.CEmptyExecutableException;
import com.cocofhu.ctb.kernel.exception.bean.CEmptyParameterException;

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

        if(parameter == null){
            throw new CEmptyParameterException("empty parameter on build a parameter wrapper. ");
        }
        if(config == null){
            throw new CEmptyConfigException();
        }
        if(executor == null){
            throw new CEmptyExecutableException("empty executable object on build a parameter wrapper. ");
        }

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
