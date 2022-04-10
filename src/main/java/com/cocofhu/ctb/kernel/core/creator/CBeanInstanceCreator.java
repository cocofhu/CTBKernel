package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.CInstantiationException;
import com.cocofhu.ctb.kernel.exception.CNoBeanFactoryException;
import com.cocofhu.ctb.kernel.exception.CNoConstructorResolverException;
import com.cocofhu.ctb.kernel.exception.CNoSuchConstructorException;



public interface CBeanInstanceCreator {

    /**
     * 根据BeanDefinition实例化
     * @return 通过指定构造器创建的对象
     * @throws CNoBeanFactoryException          在调用此方法之前，没有设置BeanFactory
     * @throws CNoConstructorResolverException  在调用此方法之前，没有设置任何一个ConstructorResolver
     * @throws CNoSuchConstructorException      没有找到相应的构造方法
     * @throws CInstantiationException          创建Bean实例时发生了错误
     */
    Object newInstance(CBeanDefinition beanDefinition)
            throws CNoBeanFactoryException, CNoConstructorResolverException, CNoSuchConstructorException, CInstantiationException;

    @SuppressWarnings("unchecked")
    default <T> T newInstance(CBeanDefinition beanDefinition,Class<T> clazz)
            throws CNoBeanFactoryException, CNoConstructorResolverException, CNoSuchConstructorException, CInstantiationException{
        return (T) newInstance(beanDefinition);
    }

    /**
     * 注册一个CConstructorResolver，用于决定构造函数
     */
    void registerConstructorResolvers(CConstructorResolver resolver);


}
