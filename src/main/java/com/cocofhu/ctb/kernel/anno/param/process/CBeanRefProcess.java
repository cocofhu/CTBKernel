package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CBeanRef;
import com.cocofhu.ctb.kernel.core.config.*;


/**
 * @author cocofhu
 */
public class CBeanRefProcess extends CAbstractAnnoProcess {

    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {
        CBeanRef annotation = parameter.getAnnotation(CBeanRef.class);
        if (annotation != null){
            String value = annotation.value();
            return new CPair<>(config.getBeanFactory().getBean(value),true);
        }
        return new CPair<>(null,false);
    }
}
