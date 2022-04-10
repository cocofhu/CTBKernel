package com.cocofhu.ctb.kernel.exception;

/**
 * 错误的BeanDefinition
 */
public class CBadBeanDefinitionException extends CNestedRuntimeException {
    public CBadBeanDefinitionException(String msg) {
        super(msg);
    }
}
