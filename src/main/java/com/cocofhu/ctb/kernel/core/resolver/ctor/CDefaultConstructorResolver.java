package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.*;

import java.lang.reflect.Constructor;

/**
 * 寻找类中唯一的一个有参构造函数，且没有无参构造函数
 * @author cocofhu
 */
public class CDefaultConstructorResolver implements CConstructorResolver {
    @Override
    public CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CConfig config, CReadOnlyDataSet<String, Object> dataSet) {
        checkEmpty(beanDefinition, config);
        Class<?> clazz = beanDefinition.getBeanClass();
        Constructor<?>[] constructors = clazz.getConstructors();
        if(constructors.length == 1 && constructors[0].getParameters().length != 0){
            return new CExecutableWrapper(constructors[0], config, beanDefinition, dataSet);
        }
        return null;
    }

    @Override
    public int priority() {
        return -1;
    }
}
