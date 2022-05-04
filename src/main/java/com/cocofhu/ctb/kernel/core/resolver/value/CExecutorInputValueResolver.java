package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.exec.CExecutorContext;


public class CExecutorInputValueResolver extends CAbstractValueResolver {


    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        Object obj = context.get(CMethodBeanFactory.EXEC_CONTEXT_KEY);
        if (obj instanceof CExecutorContext) {
            CExecutorContext executorContext = (CExecutorContext) obj;
            String name = parameter.getParameter().getName();
            Object param = executorContext.get(name);
            if (param != null) {
                return new CTBPair<>(ConverterUtils.convert(param, parameter.getParameter().getType()), true);
            }
        }

        return null;
    }
}
