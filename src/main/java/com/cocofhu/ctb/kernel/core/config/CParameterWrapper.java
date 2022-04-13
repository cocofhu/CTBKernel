package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;

import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CParameterWrapper {
    private final Parameter parameter;
    private final CTBContext context;

    public CParameterWrapper(Parameter parameter, CTBContext context) {
        this.parameter = parameter;
        this.context = context;
    }


    public List<CValueWrapper> resolveParameterValues(){
        List<CAnnotationProcess> annotationProcesses = context.getAnnotationProcesses();
        List<CValueWrapper> candidateValues = new ArrayList<>();
        for (CAnnotationProcess process:annotationProcesses){
            CTBPair<Object, Boolean> pair = process.process(this, context);
            if(pair != null && pair.getSecond()){
                candidateValues.add(new CValueWrapper(process,pair,context));
            }
        }
        return candidateValues;
    }

    public Parameter getParameter() {
        return parameter;
    }
}
