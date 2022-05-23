package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CValue;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

/**
 * @author cocofhu
 */
public class CValueProcess implements CAnnoProcess {

    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CReadOnlyData<String, Object> data) {
        CValue annotation = parameter.getAnnotation(CValue.class);
        if (annotation != null){
            String value = annotation.value();
            Object convert = ConverterUtils.convert(value, parameter.getParameter().getType());
            return new CPair<>(convert,true);
        }
        return new CPair<>(null,false);
    }
}
