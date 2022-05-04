package com.cocofhu.ctb.kernel.core.resolver.bean;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;

import java.util.ArrayList;
import java.util.List;

public interface CBeanDefinitionResolver {

    default List<CBeanDefinition> singeValue(CBeanDefinition beanDefinition){
        List<CBeanDefinition> cBeanDefinitions = new ArrayList<>();
        cBeanDefinitions.add(beanDefinition);
        return cBeanDefinitions;
    }
    List<CBeanDefinition> resolveAll();
}
