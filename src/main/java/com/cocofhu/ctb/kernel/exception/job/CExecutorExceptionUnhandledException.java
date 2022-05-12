package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 任务执行过程中存在未处理的异常
 * @author cocofhu
 */
public class CExecutorExceptionUnhandledException extends CBeanException {
    private final Throwable exception;
    public CExecutorExceptionUnhandledException(Throwable exception) {
        super("unhandled exception :" + exception.toString());
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }
}
