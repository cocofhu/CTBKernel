package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;
import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;

public class CTBContext {
    private final CBeanFactory beanFactory;
    private final CBeanInstanceCreator instanceCreator;
    private final CBeanDefinitionResolver beanDefinitionResolver;
    private final CAnnotationProcess[] annotationProcesses;

    public CTBContext(CBeanFactory beanFactory, CBeanInstanceCreator instanceCreator, CBeanDefinitionResolver beanDefinitionResolver, CAnnotationProcess[] annotationProcesses) {
        this.beanFactory = beanFactory;
        this.instanceCreator = instanceCreator;
        this.beanDefinitionResolver = beanDefinitionResolver;
        this.annotationProcesses = annotationProcesses;
    }


    public CAnnotationProcess[] getAnnotationProcesses() {
        return annotationProcesses;
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
