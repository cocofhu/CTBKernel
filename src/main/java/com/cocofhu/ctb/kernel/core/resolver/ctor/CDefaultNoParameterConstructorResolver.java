package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

/**
 * 寻找默认的无参构造函数
 * @author cocofhu
 */
public class CDefaultNoParameterConstructorResolver implements CConstructorResolver {
    @Override
    public CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CConfig config, CReadOnlyData<String, Object> data) throws CBeanException {
        checkEmpty(beanDefinition, config);
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            return new CExecutableWrapper(clazz.getConstructor(), config, beanDefinition, data);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
