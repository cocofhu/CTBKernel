package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.CDefinition;
import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;

/**
 * 寻找默认的无参构造函数
 * @author cocofhu
 */
public class CDefaultNoParameterConstructorResolver implements CConstructorResolver {
    @Override
    public CExecutableWrapper resolveConstructor(CDefinition beanDefinition, CTBContext context) {
        checkEmpty(beanDefinition,context);
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            return new CExecutableWrapper(clazz.getConstructor(),context, beanDefinition);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
