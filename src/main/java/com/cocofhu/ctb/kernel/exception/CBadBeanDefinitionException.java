package com.cocofhu.ctb.kernel.exception;

/**
 * 错误的BeanDefinition, 当一个BeanDefinition不合法时将会抛出此异常
 * @author cocofhu
 */
public class CBadBeanDefinitionException extends CNestedRuntimeException {
    public CBadBeanDefinitionException(String msg) {
        super(msg);
    }
}
