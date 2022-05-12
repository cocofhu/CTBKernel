package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 错误的任务状态,当前任务状态不符合预期
 * @author cocofhu
 */
public class CExecutorStatusException extends CBeanException {
    public CExecutorStatusException(String msg) {
        super(msg);
    }
}
