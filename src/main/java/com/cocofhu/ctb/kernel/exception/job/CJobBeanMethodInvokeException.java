package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.exception.CJobException;

/**
 * 执行Bean的方法时出错
 * @author cocofhu
 */
public class CJobBeanMethodInvokeException extends CJobException {
    private final Throwable cause;
    public CJobBeanMethodInvokeException(CExecutorMethod method, Throwable cause) {
        super("cannot call method of " + method.getMethodName() + ", exception message : " + cause.getMessage());
        this.cause = cause;
    }
    @Override
    public Throwable getCause() {
        return cause;
    }
}
