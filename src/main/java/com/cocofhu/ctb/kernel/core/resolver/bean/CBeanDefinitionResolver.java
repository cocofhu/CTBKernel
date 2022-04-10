package com.cocofhu.ctb.kernel.core.resolver.bean;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;

import java.util.List;

public interface CBeanDefinitionResolver {
    List<CBeanDefinition> resolveAll();
}
