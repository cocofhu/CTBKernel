package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;

import java.util.ArrayList;
import java.util.List;

public class CAnnotationValueResolver implements CValueResolver{

    private final CAnnotationProcess[] annotationProcesses;

    public CAnnotationValueResolver(CAnnotationProcess[] annotationProcesses) {
        this.annotationProcesses = annotationProcesses;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CTBContext context) {
        List<CValueWrapper> candidateValues = new ArrayList<>();
        for (CAnnotationProcess process:annotationProcesses){
            CTBPair<Object, Boolean> pair = process.process(parameter, context);
            if(pair != null && pair.getSecond()){
                candidateValues.add(new CValueWrapper(process,pair,context));
            }
        }
        return candidateValues;
    }
}
