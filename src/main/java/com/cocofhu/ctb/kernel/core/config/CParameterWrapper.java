package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;
import java.util.ArrayList;
import java.util.List;

public class CParameterWrapper {
    private final Parameter parameter;
    public <A extends Annotation> A findAnnotation(Class<A> annotationType){
        return parameter.getAnnotation(annotationType);
    }
    public Object resolveParameterValue(CTBContext context){
        CAnnotationProcess[] annotationProcesses = context.getAnnotationProcesses();
        List<Object> candidateValues = new ArrayList<>();
        for (CAnnotationProcess process:annotationProcesses){
            CTBPair<Object, Boolean> pair = process.processValue(parameter.getAnnotations(), context);
            if(pair.getSecond()){
                candidateValues.add(pair.getFirst());
            }
        }

        return candidateValues.get(0);
    }

    public CParameterWrapper(Parameter parameter) {
        this.parameter = parameter;
    }
}
