package com.cocofhu.ctb.kernel.exception.bean;


import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * BeanDefinition不存在
 * @author cocofhu
 */
public class CNoSuchBeanDefinitionException extends CBeanException {
    public CNoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
