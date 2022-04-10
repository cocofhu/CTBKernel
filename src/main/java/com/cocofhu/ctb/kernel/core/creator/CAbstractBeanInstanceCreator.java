package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConstructorWrapper;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.CBadBeanDefinitionException;

import java.util.ArrayList;
import java.util.List;

public abstract class CAbstractBeanInstanceCreator implements CBeanInstanceCreator, CBeanFactoryAware {

    protected List<CConstructorResolver> resolvers;
    protected CBeanFactory beanFactory;

    @Override
    public void setBeanFactory(CBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }

    protected CConstructorWrapper findDefaultConstructor(CBeanDefinition beanDefinition) throws NoSuchMethodException {
        if(beanDefinition == null){
            throw new CBadBeanDefinitionException("empty bean definition.");
        }
        Class<?> clazz = beanDefinition.getBeanClass();
        return new CConstructorWrapper(clazz.getConstructor(),new Object[0]);
    }

    @Override
    public void registerConstructorResolvers(CConstructorResolver resolver) {
        if(resolvers == null){
            resolvers = new ArrayList<>(4);
        }
        resolvers.add(resolver);
    }


}
