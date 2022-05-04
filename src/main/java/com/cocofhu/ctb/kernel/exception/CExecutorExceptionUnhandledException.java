package com.cocofhu.ctb.kernel.exception;

/**
 * 有未处理的异常
 */
public class CExecutorExceptionUnhandledException extends CNestedRuntimeException {
    private final Throwable exception;
    public CExecutorExceptionUnhandledException(Throwable exception) {
        super("unhandled exception :" + exception.toString());
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }
}
