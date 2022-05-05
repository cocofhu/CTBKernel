package com.cocofhu.ctb.kernel.core.config;


import com.cocofhu.ctb.kernel.exception.CNoParameterValueException;
import com.cocofhu.ctb.kernel.exception.CNoUniqueParameterValueException;
import com.cocofhu.ctb.kernel.exception.CUnsupportedExecutableTypeException;

import java.lang.annotation.Annotation;
import java.lang.reflect.*;
import java.util.List;

/**
 * @author cocofhu
 */
public class CExecutableWrapper implements CMateData {

    private final Executable executor;
    private final CTBContext context;
    private final CDefinition beanDefinition;

    public CExecutableWrapper(Executable executor, CTBContext context, CDefinition beanDefinition) {
        this.executor = executor;
        this.context = context;
        this.beanDefinition = beanDefinition;
    }

    private CParameterWrapper[] acquireParameterWrappers(){
        Parameter[] parameters = executor.getParameters();
        CParameterWrapper[] parameterWrappers = new CParameterWrapper[parameters.length];
        for (int i = 0 ; i< parameters.length ;++i){
            parameterWrappers[i] = new CParameterWrapper(parameters[i],context, this);
        }
        return parameterWrappers;
    }

    private Object[] acquireParameterValues() {
        CParameterWrapper[] parameterWrappers = acquireParameterWrappers();
        Object[] values = new Object[parameterWrappers.length];
        for (int i = 0; i < parameterWrappers.length; i++) {
            List<CValueWrapper> cValueWrappers = parameterWrappers[i].resolveParameterValues();


            // 以后增加更多实现
            if(cValueWrappers.size() == 0){
                throw new CNoParameterValueException("no value resolved for parameter :" +
                        parameterWrappers[i].getParameter().getName() + " on class constructor or method : " + executor.getName());
            }else if(cValueWrappers.size() != 1){
                throw new CNoUniqueParameterValueException("no unique value resolved for parameter :" +
                        parameterWrappers[i].getParameter().getName() + " on class constructor or method : " + executor.getName());
            }else{
                values[i] = cValueWrappers.get(0).getValue().getFirst();
            }
        }
        return values;
    }

    public Object execute(Object obj) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if(executor instanceof Constructor) {
            return ((Constructor<?>) executor).newInstance(acquireParameterValues());
        }else if(executor instanceof  Method){
            return ((Method) executor).invoke(obj,acquireParameterValues());
        }else{
            throw new CUnsupportedExecutableTypeException("unsupported executable type of " + executor.getClass());
        }
    }

    @Override
    public Annotation[] getAnnotations() {
        return executor.getAnnotations();
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return executor.getAnnotation(clazz);
    }

    public CDefinition getBeanDefinition() {
        return beanDefinition;
    }
}

