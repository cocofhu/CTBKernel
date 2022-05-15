package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.anno.param.process.CAnnoProcess;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public class CAnnotationValueResolver implements CValueResolver {

    private final List<CAnnoProcess> annotationProcesses;

    public CAnnotationValueResolver(List<CAnnoProcess> annotationProcesses) {
        this.annotationProcesses = annotationProcesses;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig config, CReadOnlyDataSet<String, Object> dataSet) throws CBeanException {
        List<CValueWrapper> candidateValues = new ArrayList<>();
        for (CAnnoProcess process : annotationProcesses) {
            CPair<Object, Boolean> pair = process.process(parameter,config,dataSet);
            if (pair != null && pair.getSecond()) {
                candidateValues.add(new CValueWrapper(process, pair, config, parameter));
            }
        }
        return candidateValues;
    }
}
