package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public class CAnnotationValueResolver implements CValueResolver{

    private final CProcess<CParameterWrapper>[] annotationProcesses;

    public CAnnotationValueResolver(CProcess<CParameterWrapper>[] annotationProcesses) {
        this.annotationProcesses = annotationProcesses;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CTBContext context) {
        List<CValueWrapper> candidateValues = new ArrayList<>();
        for (CProcess<CParameterWrapper> process:annotationProcesses){
            CTBPair<Object, Boolean> pair = process.process(parameter, context);
            if(pair != null && pair.getSecond()){
                candidateValues.add(new CValueWrapper(process,pair,context, parameter));
            }
        }
        return candidateValues;
    }
}
