package com.cocofhu.ctb.kernel.exception;

/**
 * 错误的任务状态
 */
public class CExecutorStatusException extends CNestedRuntimeException {
    public CExecutorStatusException(String msg) {
        super(msg);
    }
}
