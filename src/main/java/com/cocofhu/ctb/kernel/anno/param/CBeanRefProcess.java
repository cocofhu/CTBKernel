package com.cocofhu.ctb.kernel.anno.param;

import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;


public class CBeanRefProcess implements CProcess<CParameterWrapper> {

    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        CBeanRef annotation = parameter.getParameter().getAnnotation(CBeanRef.class);
        if (annotation != null){
            String value = annotation.value();
            return new CTBPair<>(context.getBeanFactory().getBean(value),true);
        }
        return new CTBPair<>(null,false);
    }
}
