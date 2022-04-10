package com.cocofhu.ctb.kernel.exception;

/**
 * 创建BeanFactory没有指定ConstructorResolver
 */
public class CNoConstructorResolverException extends CNestedRuntimeException{
    public CNoConstructorResolverException(String msg) {
        super(msg);
    }
}
