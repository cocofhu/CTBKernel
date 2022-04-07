package com.cocofhu.ctb.kernel.core.config;

import java.util.Map;

public interface CBeanDefinition {
    enum CBeanScope{
        SINGLETON,
        PROTOTYPE
    }


    String getBeanClassName();
    Class<?> getBeanClass();


    // Read-Only attributes
    boolean isSingleton();
    boolean isPrototype();
    String getBeanName();

    Map<String,Object> resourceBundles();

}

