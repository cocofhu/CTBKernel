package com.cocofhu.ctb.kernel.exception;

/**
 * BeanFactory创建时的异常
 */
public class CNoBeanFactoryException extends CNestedRuntimeException{
    public CNoBeanFactoryException(String msg) {
        super(msg);
    }
}
