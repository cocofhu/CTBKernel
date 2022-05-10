package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public class CAnnotationValueResolver implements CValueResolver {

    private final List<CProcess<CPair<CParameterWrapper, CDefaultDefaultReadOnlyDataSet>>> annotationProcesses;

    public CAnnotationValueResolver(List<CProcess<CPair<CParameterWrapper, CDefaultDefaultReadOnlyDataSet>>> annotationProcesses) {
        this.annotationProcesses = annotationProcesses;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {
        List<CValueWrapper> candidateValues = new ArrayList<>();
        for (CProcess<CPair<CParameterWrapper, CDefaultDefaultReadOnlyDataSet>> process : annotationProcesses) {
            CPair<Object, Boolean> pair = process.process(new CPair<>(parameter, dataSet), config);
            if (pair != null && pair.getSecond()) {
                candidateValues.add(new CValueWrapper(process, pair, config, parameter));
            }
        }
        return candidateValues;
    }
}
