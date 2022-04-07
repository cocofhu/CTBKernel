package com.cocofhu.ctb.kernel.core.config;

public interface CBeanDefinition {
    enum CBeanScope{
        SINGLETON,
        PROTOTYPE
    }


    String getBeanClassName();
    Class<?> getBeanClass();


    CConstructorExecutionWrapper resolveConstructor();


    // Read-Only attributes
    boolean isSingleton();
    boolean isPrototype();
    String getBeanName();
}

