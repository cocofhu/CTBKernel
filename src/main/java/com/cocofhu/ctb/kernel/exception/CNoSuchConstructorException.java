package com.cocofhu.ctb.kernel.exception;

/**
 * 没有构造器
 */
public class CNoSuchConstructorException extends CNestedRuntimeException{
    public CNoSuchConstructorException(String msg) {
        super(msg);
    }
}
