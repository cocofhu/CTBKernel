package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CValue;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

/**
 * @author cocofhu
 */
public class CValueProcess implements CProcess<CParameterWrapper> {

    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        CValue annotation = parameter.getAnnotation(CValue.class);
        if (annotation != null){
            String value = annotation.value();
            Object convert = ConverterUtils.convert(value, parameter.getParameter().getType());
            return new CTBPair<>(convert,true);
        }
        return new CTBPair<>(null,false);
    }
}
