package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.exec.CExecutorInput;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

public class CExecutorInputProcess implements CAnnoProcess {



    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CReadOnlyDataSet<String, Object> dataSet) {

        CExecutorInput attachmentArgs = parameter.acquireNearAnnotation(CExecutorInput.class);
        if (attachmentArgs != null) {
            String name = attachmentArgs.name();
            if(CStringUtils.isEmpty(name)) {
                name = parameter.getParameter().getName();
            }
            Object param = dataSet.get(name);
            if(param != null){
                if (parameter.getParameter().getType().isAssignableFrom(param.getClass())) {
                    return new CPair<>(param, true);
                }
                return new CPair<>(ConverterUtils.convert(param, parameter.getParameter().getType()), true);
            }

            if(attachmentArgs.nullable()){
                return new CPair<>(null, true);
            }
        }



        return null;
    }
}
