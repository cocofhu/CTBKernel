package com.cocofhu.ctb.kernel.exception;


/**
 * BeanDefinition不存在
 * @author cocofhu
 */
public class CNoSuchBeanDefinitionException extends CNestedRuntimeException {
    public CNoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
