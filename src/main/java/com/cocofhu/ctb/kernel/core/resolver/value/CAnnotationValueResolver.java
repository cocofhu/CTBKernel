package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.exception.CBeanException;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public class CAnnotationValueResolver implements CValueResolver {

    private final List<CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>>> annotationProcesses;

    public CAnnotationValueResolver(List<CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>>> annotationProcesses) {
        this.annotationProcesses = annotationProcesses;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig config, CReadOnlyDataSet<String, Object> dataSet) throws CBeanException {
        List<CValueWrapper> candidateValues = new ArrayList<>();
        for (CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> process : annotationProcesses) {
            CPair<Object, Boolean> pair = process.process(new CPair<>(parameter, dataSet), config);
            if (pair != null && pair.getSecond()) {
                candidateValues.add(new CValueWrapper(process, pair, config, parameter));
            }
        }
        return candidateValues;
    }
}
