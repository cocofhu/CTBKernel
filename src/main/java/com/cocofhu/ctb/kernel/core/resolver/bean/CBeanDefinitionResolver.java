package com.cocofhu.ctb.kernel.core.resolver.bean;

import com.cocofhu.ctb.kernel.core.config.CDefinition;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public interface CBeanDefinitionResolver {

    default List<CDefinition> singeValue(CDefinition beanDefinition){
        List<CDefinition> cBeanDefinitions = new ArrayList<>();
        cBeanDefinitions.add(beanDefinition);
        return cBeanDefinitions;
    }
    List<CDefinition> resolveAll();
}
