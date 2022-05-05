package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;

/**
 * @author cocofhu
 */
public class CExecutorContextValueResolver extends CAbstractValueResolver {
    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        if(parameter.getParameter().getType() == (CExecutorContext.class)){
            Object obj = context.get(CExecutor.EXEC_CONTEXT_KEY);
            if (obj instanceof CExecutorContext) {
                return new CTBPair<>(obj,true);
            }

        }
        return null;
    }
}
