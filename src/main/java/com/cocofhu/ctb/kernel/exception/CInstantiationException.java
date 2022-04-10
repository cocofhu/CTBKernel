package com.cocofhu.ctb.kernel.exception;

/**
 * 初始化Bean的时候会出现的异常
 */
public class CInstantiationException extends CNestedRuntimeException {
    public CInstantiationException(String msg) {
        super(msg);
    }
}
