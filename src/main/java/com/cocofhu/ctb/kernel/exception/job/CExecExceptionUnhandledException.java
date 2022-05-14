package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CJobException;

/**
 * 任务执行过程中存在未处理的异常
 * @author cocofhu
 */
public class CExecExceptionUnhandledException extends CJobException {
    private final Throwable exception;
    public CExecExceptionUnhandledException(Throwable exception) {
        super("unhandled exception :" + exception.toString());
        this.exception = exception;
    }

    public Throwable getException() {
        return exception;
    }
}