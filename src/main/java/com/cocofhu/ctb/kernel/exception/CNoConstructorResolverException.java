package com.cocofhu.ctb.kernel.exception;

/**
 * 创建BeanFactory没有指定ConstructorResolver
 * @author cocofhu
 */
public class CNoConstructorResolverException extends CNestedRuntimeException{
    public CNoConstructorResolverException(String msg) {
        super(msg);
    }
}
