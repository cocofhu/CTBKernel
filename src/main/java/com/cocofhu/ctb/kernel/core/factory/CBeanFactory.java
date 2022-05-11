package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.config.CReadOnlyDataSet;
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

    CConfig getConfig();

    CBeanDefinition getBeanDefinition(String name);
    CBeanDefinition getBeanDefinition(String name, Class<?> requiredType);
    CBeanDefinition getBeanDefinition(Class<?> requiredType);

    Object getBean(CBeanDefinition beanDefinition) ;

    Object getBean(String name, CReadOnlyDataSet<String, Object> dataSet) ;
    <T> T getBean(String name, Class<T> requiredType, CReadOnlyDataSet<String, Object> dataSet) ;
    <T> T getBean(Class<T> requiredType, CReadOnlyDataSet<String, Object> dataSet);

    Object getBean(CBeanDefinition beanDefinition, CReadOnlyDataSet<String, Object> dataSet) ;


}
