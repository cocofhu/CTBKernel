package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CBeanRef;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;


/**
 * @author cocofhu
 */
public class CBeanRefProcess implements CAnnoProcess {

    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CReadOnlyDataSet<String, Object> dataSet) {
        CBeanRef annotation = parameter.getAnnotation(CBeanRef.class);
        if (annotation != null){
            String value = annotation.value();
            return new CPair<>(config.getBeanFactory().getBean(value),true);
        }
        return new CPair<>(null,false);
    }
}
