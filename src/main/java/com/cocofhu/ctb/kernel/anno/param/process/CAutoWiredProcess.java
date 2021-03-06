package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.bean.CNoSuchBeanDefinitionException;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

import java.lang.annotation.Annotation;

import static com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime.EXEC_CONTEXT_KEY;

public class CAutoWiredProcess implements CAnnoProcess {
    @Override
    public CPair<Object, Boolean> process(CParameterWrapper parameter, CConfig config, CReadOnlyData<String, Object> data) {
        Annotation annotation = parameter.acquireNearAnnotation(CAutowired.class);
        if (annotation != null && CBeanFactory.class.isAssignableFrom(parameter.getParameter().getType())) {
            return new CPair<>(config.getBeanFactory(), true);
        }

        if (annotation != null && CExecutionRuntime.class.isAssignableFrom(parameter.getParameter().getType())) {
            Object o = data.get(EXEC_CONTEXT_KEY);
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
