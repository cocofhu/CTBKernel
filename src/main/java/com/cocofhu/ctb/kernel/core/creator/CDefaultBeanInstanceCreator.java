package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.exception.bean.CInstantiationException;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

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


    public CDefaultBeanInstanceCreator(CConstructorResolver[] constructorResolvers) {
        for (CConstructorResolver ctorResolver: constructorResolvers) {
            registerConstructorResolvers(ctorResolver);
        }
    }

    @Override
    public Object newInstance(CBeanDefinition beanDefinition, CConfig config, CReadOnlyData<String, Object> dataSet)
            throws CBeanException {
        try {
            return resolveConstructor(beanDefinition, config,dataSet).execute(null);
        } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
            throw new CInstantiationException("Instantiating for " + beanDefinition.getBeanClassName() + " failed.", beanDefinition);
        }
    }

}
