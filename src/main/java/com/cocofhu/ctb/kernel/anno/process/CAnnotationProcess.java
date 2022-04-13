package com.cocofhu.ctb.kernel.anno.process;

import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CTBContext;

import java.lang.annotation.Annotation;

public interface CAnnotationProcess {
    CTBPair<Object,Boolean> processValue(Annotation[] annotations, CTBContext context);
}
