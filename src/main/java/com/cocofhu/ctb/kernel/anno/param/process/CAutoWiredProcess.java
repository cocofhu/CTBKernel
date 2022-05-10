package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.CNoSuchBeanDefinitionException;

import java.lang.annotation.Annotation;

public class CAutoWiredProcess extends CAbstractAnnoProcess {
    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {
        Annotation annotation = parameter.acquireNearAnnotation(CAutowired.class);
        if (annotation != null && parameter.getParameter().getType().isAssignableFrom(CBeanFactory.class)) {
            return new CPair<>(config.getBeanFactory(), true);
        }
        if (annotation != null && parameter.getParameter().getType().isAssignableFrom(CExecutorContext.class)) {
            Object obj = dataSet.get(CExecutor.EXEC_CONTEXT_KEY);
            if (obj instanceof CExecutorContext) {
                return new CPair<>(obj, true);
            }
        }
        try {
            Object bean = config.getBeanFactory().getBean(parameter.getParameter().getType());
            return new CPair<>(bean, true);
        } catch (CNoSuchBeanDefinitionException ignored) {

        }
        return null;
    }
}
