package com.cocofhu.ctb.kernel.exception;

/**
 * 运行时异常
 * @author cocofhu
 */
public class CNestedRuntimeException extends RuntimeException{
    public CNestedRuntimeException(String msg) {
        super(msg);
    }
    public CNestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
