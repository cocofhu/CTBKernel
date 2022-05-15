package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

/**
 * 寻找默认的无参构造函数
 * @author cocofhu
 */
public class CDefaultNoParameterConstructorResolver implements CConstructorResolver {
    @Override
    public CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CConfig config, CReadOnlyDataSet<String, Object> dataSet) throws CBeanException {
        checkEmpty(beanDefinition, config);
        Class<?> clazz = beanDefinition.getBeanClass();
        try {
            return new CExecutableWrapper(clazz.getConstructor(), config, beanDefinition, dataSet);
        } catch (NoSuchMethodException e) {
            return null;
        }
    }
}
