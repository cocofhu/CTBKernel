package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.CInstantiationException;
import com.cocofhu.ctb.kernel.exception.CNoBeanFactoryException;
import com.cocofhu.ctb.kernel.exception.CNoConstructorResolverException;
import com.cocofhu.ctb.kernel.exception.CNoSuchConstructorException;

import java.lang.reflect.InvocationTargetException;

/**
 * Bean实例创建器
 * 0、使用 newInstance 实例化Bean
 * 1、通过 registerConstructorResolvers 注册 ConstructorResolver 来寻找构造函数
 * 2、寻找到构造函数(CConstructorWrapper)后，调用 acquireParameterValues获得构造函数需要的参数
 * 3、调用 newInstance 实例化对象
 * @author cocofhu
 */
public class CDefaultBeanInstanceCreator extends CAbstractBeanInstanceCreator {

    public CDefaultBeanInstanceCreator() {
    }
    public CDefaultBeanInstanceCreator(CConstructorResolver[] constructorResolvers) {
        for (CConstructorResolver ctorResolver: constructorResolvers) {
            registerConstructorResolvers(ctorResolver);
        }
    }

    @Override
    public Object newInstance(CDefinition beanDefinition, CTBContext context)
            throws CNoBeanFactoryException, CNoConstructorResolverException, CNoSuchConstructorException, CInstantiationException {

        if (resolvers == null || resolvers.size() == 0) {
            throw new CNoConstructorResolverException("ConstructorResolver set was not set.");
        }
        CExecutableWrapper ctorWrapper = null;
        for (CConstructorResolver resolver : resolvers) {
            ctorWrapper = resolver.resolveConstructor(beanDefinition, context);
            if (ctorWrapper != null) break;
        }
        if (ctorWrapper == null) {
            throw new CNoSuchConstructorException("No constructor was found for " + beanDefinition.getBeanClassName() + ".");
        }
        try {
            return ctorWrapper.execute(null);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new CInstantiationException("Instantiating for " + beanDefinition.getBeanClassName() + " failed.");
        }
    }
}
