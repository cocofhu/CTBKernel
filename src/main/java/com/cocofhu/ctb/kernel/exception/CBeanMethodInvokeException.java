package com.cocofhu.ctb.kernel.exception;

/**
 * 执行Bean的方法时出错
 */
public class CBeanMethodInvokeException extends CNestedRuntimeException {
    public CBeanMethodInvokeException(String msg) {
        super(msg);
    }
}
