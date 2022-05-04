package com.cocofhu.ctb.kernel.exception;

/**
 * 执行Bean的方法时出错
 */
public class CMethodBeanInvokeException extends CNestedRuntimeException {
    public CMethodBeanInvokeException(String msg) {
        super(msg);
    }
}
