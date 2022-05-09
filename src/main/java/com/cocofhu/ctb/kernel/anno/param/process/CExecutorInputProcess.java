package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CExecutorInput;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

public class CExecutorInputProcess implements CProcess<CParameterWrapper> {

    private CTBPair<Object, Boolean> getFromKey(CParameterWrapper parameter, CTBContext context, String key) {
        Object obj = context.get(key);
        if (obj instanceof CExecutorContext) {
            CExecutorContext executorContext = (CExecutorContext) obj;
            String name = parameter.getParameter().getName();
            Object param = executorContext.get(name);
            if (param != null) {
                if (parameter.getParameter().getType().isAssignableFrom(param.getClass())) {
                    return new CTBPair<>(param, true);
                }
                return new CTBPair<>(ConverterUtils.convert(param, parameter.getParameter().getType()), true);
            }
        }
        return null;
    }

    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {

        CExecutorInput attachmentArgs = parameter.acquireNearAnnotation(CExecutorInput.class);
        CTBPair<Object, Boolean> obj = null;
        if (attachmentArgs != null) {
            obj = getFromKey(parameter, context, CExecutor.EXEC_ATTACHMENT_KEY);
        }
        if (obj == null) {
            obj = getFromKey(parameter, context, CExecutor.EXEC_CONTEXT_KEY);
        }
        return obj;
    }
}
