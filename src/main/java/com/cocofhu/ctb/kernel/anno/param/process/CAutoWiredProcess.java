package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.bean.CNoSuchBeanDefinitionException;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

import java.lang.annotation.Annotation;

import static com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime.EXEC_CONTEXT_KEY;

public class CAutoWiredProcess implements CAnnoProcess {
    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CReadOnlyDataSet<String, Object> dataSet) {
        Annotation annotation = parameter.acquireNearAnnotation(CAutowired.class);
        if (annotation != null && parameter.getParameter().getType().isAssignableFrom(CBeanFactory.class)) {
            return new CPair<>(config.getBeanFactory(), true);
        }
        if (annotation != null && parameter.getParameter().getType().isAssignableFrom(CExecutionRuntime.class)) {
            Object o = dataSet.get(EXEC_CONTEXT_KEY);
            if (o instanceof CExecutionRuntime) {
                return new CPair<>(o, true);
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
