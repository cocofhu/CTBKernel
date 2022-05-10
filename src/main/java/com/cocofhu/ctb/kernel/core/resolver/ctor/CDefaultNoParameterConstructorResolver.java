package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.*;

/**
 * 寻找默认的无参构造函数
 * @author cocofhu
 */
public class CDefaultNoParameterConstructorResolver implements CConstructorResolver {
    @Override
    public CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {
        checkEmpty(beanDefinition, config);
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            return new CExecutableWrapper(clazz.getConstructor(), config, beanDefinition, dataSet);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
