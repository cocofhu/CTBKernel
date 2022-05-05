package com.cocofhu.ctb.kernel.exception;

/**
 * 任务执行过程中存在未处理的异常
 * @author cocofhu
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
