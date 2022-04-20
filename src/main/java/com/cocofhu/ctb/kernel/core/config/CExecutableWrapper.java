package com.cocofhu.ctb.kernel.core.config;


import com.cocofhu.ctb.kernel.anno.CConstructor;
import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.exception.CNoParameterValueException;
import com.cocofhu.ctb.kernel.exception.CNoUniqueParameterValueException;

import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Method;
import java.lang.reflect.Parameter;
import java.util.List;


public class CExecutableWrapper {

    private final Executable executor;
    private final CTBContext context;

    public CExecutableWrapper(Executable executor, CTBContext context) {
        this.executor = executor;
        this.context = context;

    }

    public CParameterWrapper[] acquireParameterWrappers(){
        Parameter[] parameters = executor.getParameters();
        CParameterWrapper[] parameterWrappers = new CParameterWrapper[parameters.length];
        for (int i = 0 ; i< parameters.length ;++i){
            parameterWrappers[i] = new CParameterWrapper(parameters[i],context);
        }
        return parameterWrappers;
    }

    public Object[] acquireParameterValues() {
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

    public Constructor<?> getConstructor() {
        return (Constructor<?>) executor;
    }

    public Method getMethod() {
        return (Method) executor;
    }

}

