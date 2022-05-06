package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.anno.CAutowired;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.CNoSuchBeanDefinitionException;

import java.lang.annotation.Annotation;

/**
 * @author cocofhu
 */
public class CAutowiredValueResolver extends CAbstractValueResolver {
    @Override
    public CTBPair<Object, Boolean> process(CParameterWrapper parameter, CTBContext context) {
        Annotation annotation = parameter.acquireNearAnnotation(CAutowired.class);
        if (annotation != null && parameter.getParameter().getType().isAssignableFrom(CBeanFactory.class)) {
            return new CTBPair<>(context.getBeanFactory(), true);
        }
        if (annotation != null && parameter.getParameter().getType().isAssignableFrom(CExecutorContext.class)) {
            Object obj = context.get(CExecutor.EXEC_CONTEXT_KEY);
            if (obj instanceof CExecutorContext) {
                return new CTBPair<>(obj, true);
            }
        }

        try {
            Object bean = context.getBeanFactory().getBean(parameter.getParameter().getType());
            return new CTBPair<>(bean, true);
        } catch (CNoSuchBeanDefinitionException ignored) {

        }


        return null;
    }
}
