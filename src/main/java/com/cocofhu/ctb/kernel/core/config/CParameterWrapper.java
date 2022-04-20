package com.cocofhu.ctb.kernel.core.config;

import java.lang.reflect.Parameter;
import java.util.List;

public class CParameterWrapper {
    private final Parameter parameter;
    private final CTBContext context;

    public CParameterWrapper(Parameter parameter, CTBContext context) {
        this.parameter = parameter;
        this.context = context;
    }


    public List<CValueWrapper> resolveParameterValues(){
        return context.getValueResolver().resolveValues(this,context);
    }

    public Parameter getParameter() {
        return parameter;
    }
}
