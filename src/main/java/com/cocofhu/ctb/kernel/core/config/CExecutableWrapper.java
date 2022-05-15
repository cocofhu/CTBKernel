package com.cocofhu.ctb.kernel.core.config;


import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.exception.bean.*;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;

/**
 *
 * @author cocofhu
 */
public class CExecutableWrapper implements CMateData {

    private final Executable executor;
    private final CConfig config;
    private final CBeanDefinition beanDefinition;

    private final CReadOnlyDataSet<String, Object> dataSet;

    public CExecutableWrapper(Executable executor, CConfig config, CBeanDefinition beanDefinition, CReadOnlyDataSet<String, Object> dataSet) throws CBeanException {

        if (executor == null) {
            throw new CNoExecutableException("empty executable object, maybe method or constructor not found.");
        }
        if (config == null) {
            throw new CNoConfigException();
        }
        if (beanDefinition == null) {
            throw new CNoBeanDefinitionException();
        }


        this.executor = executor;
        this.config = config;
        this.beanDefinition = beanDefinition;
        this.dataSet = dataSet;
    }

    public CParameterWrapper[] acquireParameterWrappers() {
        Parameter[] parameters = executor.getParameters();
        CParameterWrapper[] parameterWrappers = new CParameterWrapper[parameters.length];
        for (int i = 0 ; i< parameters.length ;++i){
            parameterWrappers[i] = new CParameterWrapper(parameters[i], config, this, dataSet);
        }
        return parameterWrappers;
    }

    public Object execute(Object obj) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if(executor instanceof Constructor) {
            return ((Constructor<?>) executor).newInstance(acquireParameterValues());
        }else if(executor instanceof  Method){
            return ((Method) executor).invoke(obj,acquireParameterValues());
        }else{
            throw new CUnsupportedExecutableTypeException(this);
        }
    }

    public Executable getExecutor() {
        return executor;
    }

    @Override
    public Annotation[] getAnnotations() {
        return executor.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return executor.getAnnotation(clazz);
    }

    @Override
    public CMateData getParent() {
        return beanDefinition;
    }

    private Object[] acquireParameterValues() {
        CParameterWrapper[] parameterWrappers = acquireParameterWrappers();
        Object[] values = new Object[parameterWrappers.length];
        for (int i = 0; i < parameterWrappers.length; i++) {
            List<CValueWrapper> cValueWrappers = parameterWrappers[i].resolveParameterValues();


            // 以后增加更多实现
            if(cValueWrappers.size() == 0){
                throw new CNoParameterValueException(parameterWrappers[i], this);
            }else if(cValueWrappers.size() != 1){
                throw new CNoUniqueParameterValueException(parameterWrappers[i], this, cValueWrappers);
            }else{
                values[i] = cValueWrappers.get(0).getValue().getFirst();
            }
        }
        return values;
    }
}

