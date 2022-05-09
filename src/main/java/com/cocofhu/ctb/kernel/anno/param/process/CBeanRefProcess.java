package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CBeanRef;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;


/**
 * @author cocofhu
 */
public class CBeanRefProcess implements CProcess<CParameterWrapper> {

    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        CBeanRef annotation = parameter.getAnnotation(CBeanRef.class);
        if (annotation != null){
            String value = annotation.value();
            return new CTBPair<>(context.getBeanFactory().getBean(value),true);
        }
        return new CTBPair<>(null,false);
    }
}