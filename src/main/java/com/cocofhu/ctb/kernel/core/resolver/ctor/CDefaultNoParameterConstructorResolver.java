package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConstructorWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;

/**
 * 寻找默认的无参构造函数
 */
public class CDefaultNoParameterConstructorResolver implements CConstructorResolver {
    @Override
    public CConstructorWrapper resolveConstructor(CBeanDefinition beanDefinition,CTBContext context) {
        checkEmpty(beanDefinition,context);
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            return new CConstructorWrapper(clazz.getConstructor(),context);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
