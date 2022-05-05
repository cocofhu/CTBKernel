package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.config.CDefinition;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.exception.CNoSuchBeanDefinitionException;

/**
 * @author cocofhu
 */
public interface CBeanFactory {

    Object getBean(String name) ;
    <T> T getBean(String name, Class<T> requiredType) ;
    <T> T getBean(Class<T> requiredType);
    boolean containsBean(String name);
    boolean isSingleton(String name) throws CNoSuchBeanDefinitionException;
    boolean isPrototype(String name) throws CNoSuchBeanDefinitionException;
    Class<?> getType(String name) throws CNoSuchBeanDefinitionException;

    CTBContext getContext();

    CDefinition getBeanDefinition(String name);
    CDefinition getBeanDefinition(String name, Class<?> requiredType);
    CDefinition getBeanDefinition(Class<?> requiredType);

    Object getBean(CDefinition beanDefinition) ;




}
