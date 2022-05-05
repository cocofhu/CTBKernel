package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.anno.CExecutorContextPrefix;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;

/**
 * @author cocofhu
 */
public class CExecutorInputValueResolver extends CAbstractValueResolver {


    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        CExecutorContextPrefix executorContextPrefix = parameter.acquireNearAnnotation(CExecutorContextPrefix.class);
        String prefix = "";
        if(executorContextPrefix != null){
            prefix = executorContextPrefix.value() ;
        }
        Object obj = context.get(CExecutor.EXEC_CONTEXT_KEY);
        if (obj instanceof CExecutorContext) {
            CExecutorContext executorContext = (CExecutorContext) obj;
            String name = parameter.getParameter().getName();
            Object param = executorContext.get(prefix + name);
            if (param != null) {
                return new CTBPair<>(ConverterUtils.convert(param, parameter.getParameter().getType()), true);
            }
        }

        return null;
    }
}
