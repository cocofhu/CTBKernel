package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConstructorWrapper;
import com.cocofhu.ctb.kernel.exception.CBadBeanDefinitionException;

public class CDefaultConstructorResolver implements CConstructorResolver{
    @Override
    public CConstructorWrapper resolveConstructor(CBeanDefinition beanDefinition) {
        if(beanDefinition == null){
            throw new CBadBeanDefinitionException("empty bean definition.");
        }
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            return new CConstructorWrapper(clazz.getConstructor(),new Object[0]);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
