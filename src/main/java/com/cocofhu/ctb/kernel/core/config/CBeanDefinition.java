package com.cocofhu.ctb.kernel.core.config;


import java.lang.reflect.Method;

/**
 * @author cocofhu
 */
public interface CBeanDefinition extends CMateData {

    enum CBeanScope{
        /**
         * 单例模式
         */
        SINGLETON,
        /**
         * 原型模式
         */
        PROTOTYPE
    }


    String getBeanClassName();
    Class<?> getBeanClass();


    // Read-Only attributes
    boolean isSingleton();
    boolean isPrototype();
    String getBeanName();

    default Method[] initMethods() {
        return new Method[0];
    }

}

