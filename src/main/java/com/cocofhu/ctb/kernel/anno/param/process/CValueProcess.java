package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CValue;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.*;

/**
 * @author cocofhu
 */
public class CValueProcess extends CAbstractAnnoProcess {

    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {
        CValue annotation = parameter.getAnnotation(CValue.class);
        if (annotation != null){
            String value = annotation.value();
            Object convert = ConverterUtils.convert(value, parameter.getParameter().getType());
            return new CPair<>(convert,true);
        }
        return new CPair<>(null,false);
    }
}
