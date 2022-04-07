package com.cocofhu.ctb.kernel;

public class NestedRuntimeException extends RuntimeException{
    public NestedRuntimeException(String msg) {
        super(msg);
    }
    public NestedRuntimeException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
