package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * @author cocofhu
 */
public interface CBeanFactory {

    Object getBean(String name) throws CBeanException;
    <T> T getBean(String name, Class<T> requiredType) throws CBeanException ;
    <T> T getBean(Class<T> requiredType) throws CBeanException;
    boolean isSingleton(String name) throws CBeanException;
    boolean isPrototype(String name) throws CBeanException;
    Class<?> getType(String name) throws CBeanException;

    CConfig getConfig();
    boolean containsBean(String name);

    CBeanDefinition getBeanDefinition(String name) throws CBeanException;
    CBeanDefinition getBeanDefinition(String name, Class<?> requiredType) throws CBeanException;
    CBeanDefinition getBeanDefinition(Class<?> requiredType) throws CBeanException;

    Object getBean(CBeanDefinition beanDefinition) throws CBeanException;

    Object getBean(String name, CReadOnlyData<String, Object> data) throws CBeanException;
    <T> T getBean(String name, Class<T> requiredType, CReadOnlyData<String, Object> data) throws CBeanException;
    <T> T getBean(Class<T> requiredType, CReadOnlyData<String, Object> data)throws CBeanException;

    Object getBean(CBeanDefinition beanDefinition, CReadOnlyData<String, Object> data) throws CBeanException;


}
