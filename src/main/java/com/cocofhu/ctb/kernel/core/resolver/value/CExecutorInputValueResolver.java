package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.anno.CAttachmentArgs;
import com.cocofhu.ctb.kernel.anno.CExecutorMethod;
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


    private CTBPair<Object, Boolean> getFromKey(CParameterWrapper parameter, CTBContext context, String key) {
        Object obj = context.get(key);
        if (obj instanceof CExecutorContext) {
            CExecutorContext executorContext = (CExecutorContext) obj;
            String name = parameter.getParameter().getName();
            Object param = executorContext.get(name);
            if (param != null) {
                if(parameter.getParameter().getType().isAssignableFrom(param.getClass())){
                    return new CTBPair<>(param, true);
                }
                return new CTBPair<>(ConverterUtils.convert(param, parameter.getParameter().getType()), true);
            }
        }
        return null;
    }
    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        CExecutorMethod executorMethod = parameter.acquireNearAnnotation(CExecutorMethod.class);
        if(executorMethod == null){
            return null;
        }
        CAttachmentArgs attachmentArgs = parameter.acquireNearAnnotation(CAttachmentArgs.class);
        CTBPair<Object,Boolean> obj = null;
        if(attachmentArgs != null){
            obj = getFromKey(parameter,context,CExecutor.EXEC_ATTACHMENT_KEY);
        }
        if(obj == null){
            obj =  getFromKey(parameter,context,CExecutor.EXEC_CONTEXT_KEY);
        }
        return obj;
    }
}
