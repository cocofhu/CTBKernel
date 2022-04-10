package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConstructorWrapper;

@FunctionalInterface
public interface CConstructorResolver {
    CConstructorWrapper resolveConstructor(CBeanDefinition beanDefinition);
}
