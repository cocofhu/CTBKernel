package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.exception.CBeansException;
import com.cocofhu.ctb.kernel.exception.CNoSuchBeanDefinitionException;

public interface CBeanFactory {

    Object getBean(String name) throws CBeansException;
    <T> T getBean(String name, Class<T> requiredType) throws CBeansException;
    <T> T getBean(Class<T> requiredType) throws CBeansException;
    boolean containsBean(String name);
    boolean isSingleton(String name) throws CNoSuchBeanDefinitionException;
    boolean isPrototype(String name) throws CNoSuchBeanDefinitionException;
    Class<?> getType(String name) throws CNoSuchBeanDefinitionException;

    CTBContext getContext();




}
