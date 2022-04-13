package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;
import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;

import java.util.Collections;
import java.util.List;

public class CTBContext {
    private final CBeanFactory beanFactory;
    private final CBeanInstanceCreator instanceCreator;
    private final CBeanDefinitionResolver beanDefinitionResolver;
    private final List<CAnnotationProcess> parameterAnnotationProcesses;

    public CTBContext(CBeanFactory beanFactory, CBeanInstanceCreator instanceCreator, CBeanDefinitionResolver beanDefinitionResolver, List<CAnnotationProcess> parameterAnnotationProcesses) {
        this.beanFactory = beanFactory;
        this.instanceCreator = instanceCreator;
        this.beanDefinitionResolver = beanDefinitionResolver;
        this.parameterAnnotationProcesses = parameterAnnotationProcesses;
    }

    public List<CAnnotationProcess> getAnnotationProcesses() {
        return Collections.unmodifiableList(parameterAnnotationProcesses);
    }

    public CBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public CBeanDefinitionResolver getBeanDefinitionResolver() {
        return beanDefinitionResolver;
    }

    public CBeanInstanceCreator getInstanceCreator() {
        return instanceCreator;
    }
}
