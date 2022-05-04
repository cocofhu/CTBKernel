package com.cocofhu.ctb.kernel.exception;

/**
 * 未知的异常
 */
public class CExecutorExceptionUnknownException extends CNestedRuntimeException {
    private final Object exception;
    public CExecutorExceptionUnknownException(Object exception) {
        super("unhandled exception :" + exception);
        this.exception = exception;
    }

    public Object getException() {
        return exception;
    }
}
