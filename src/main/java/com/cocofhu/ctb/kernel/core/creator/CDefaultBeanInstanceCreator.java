package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConstructorWrapper;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.CInstantiationException;
import com.cocofhu.ctb.kernel.exception.CNoBeanFactoryException;
import com.cocofhu.ctb.kernel.exception.CNoConstructorResolverException;
import com.cocofhu.ctb.kernel.exception.CNoSuchConstructorException;

import java.lang.reflect.InvocationTargetException;

public class CDefaultBeanInstanceCreator extends CAbstractBeanInstanceCreator{

    @Override
    public Object newInstance(CBeanDefinition beanDefinition)
            throws CNoBeanFactoryException,CNoConstructorResolverException,CNoSuchConstructorException,CInstantiationException{

        if(resolvers == null || resolvers.size() == 0){
            throw new CNoConstructorResolverException("ConstructorResolver set was not set.");
        }
        CConstructorWrapper ctorWrapper = null ;
        for (CConstructorResolver resolver:resolvers){
            ctorWrapper = resolver.resolveConstructor(beanDefinition);
            if (ctorWrapper != null) break;
        }
        if(ctorWrapper == null){
            throw new CNoSuchConstructorException("No constructor was found for " + beanDefinition.getBeanClassName() + ".");
        }
        try {
            return ctorWrapper.getConstructor().newInstance(ctorWrapper.getParameters());
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new CInstantiationException("Instantiating for "+ beanDefinition.getBeanClassName()+" failed.");
        }
    }
}
