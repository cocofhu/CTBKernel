package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;

public class CValueWrapper {
    private final CAnnotationProcess annotationProcess;
    private final CTBPair<Object,Boolean> value;
    private final CTBContext context;

    public CValueWrapper(CAnnotationProcess annotationProcess, CTBPair<Object, Boolean> value, CTBContext context) {
        this.annotationProcess = annotationProcess;
        this.value = value;
        this.context = context;
    }


    public CAnnotationProcess getAnnotationProcess() {
        return annotationProcess;
    }

    public CTBPair<Object, Boolean> getValue() {
        return value;
    }
}
