package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;

/**
 * @author cocofhu
 */
public class CBeanFactoryValueResolver extends CAbstractValueResolver {
    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        if(parameter.getParameter().getType().isAssignableFrom(CBeanFactory.class)){
            return new CTBPair<>(context.getBeanFactory(),true);
        }
        return null;
    }
}
