package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CExecutorInput;
import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;

public class CExecutorInputProcess extends CAbstractAnnoProcess {

    private CPair<Object, Boolean> getFromKey(CParameterWrapper parameter, CDefaultDefaultReadOnlyDataSet dataSet, String key) {
        Object obj = dataSet.get(key);
        if (obj instanceof CExecutorContext executorContext) {
            String name = parameter.getParameter().getName();
            Object param = executorContext.get(name);
            if (param != null) {
                if (parameter.getParameter().getType().isAssignableFrom(param.getClass())) {
                    return new CPair<>(param, true);
                }
                return new CPair<>(ConverterUtils.convert(param, parameter.getParameter().getType()), true);
            }
        }
        return null;
    }

    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {

        CExecutorInput attachmentArgs = parameter.acquireNearAnnotation(CExecutorInput.class);
        CPair<Object, Boolean> obj = null;
        if (attachmentArgs != null) {
            obj = getFromKey(parameter, dataSet, CExecutor.EXEC_ATTACHMENT_KEY);
        }
        if (obj == null) {
            obj = getFromKey(parameter, dataSet, CExecutor.EXEC_CONTEXT_KEY);
        }
        return obj;
    }
}
