package com.cocofhu.ctb.kernel.exception;

public class CNestedRuntimeException extends RuntimeException{
    public CNestedRuntimeException(String msg) {
        super(msg);
    }
    public CNestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
