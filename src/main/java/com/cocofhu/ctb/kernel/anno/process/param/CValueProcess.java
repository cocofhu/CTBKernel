package com.cocofhu.ctb.kernel.anno.process.param;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;


public class CValueProcess implements CAnnotationProcess {

    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        CValue annotation = parameter.getParameter().getAnnotation(CValue.class);
        if (annotation != null){
            String value = annotation.value();
            Object convert = ConverterUtils.convert(value, parameter.getParameter().getType());
            return new CTBPair<>(convert,true);
        }
        return new CTBPair<>(null,false);
    }
}
