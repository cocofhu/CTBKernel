package com.cocofhu.ctb.kernel.exception;

/**
 * 错误的任务状态,当前任务状态不符合预期
 * @author cocofhu
 */
public class CExecutorStatusException extends CNestedRuntimeException {
    public CExecutorStatusException(String msg) {
        super(msg);
    }
}
