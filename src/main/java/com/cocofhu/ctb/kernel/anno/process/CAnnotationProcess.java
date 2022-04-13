package com.cocofhu.ctb.kernel.anno.process;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CTBContext;

import java.lang.annotation.Annotation;
import java.lang.reflect.Parameter;

public interface CAnnotationProcess {
    CTBPair<Object,Boolean> process(CParameterWrapper parameter, CTBContext context);
}
