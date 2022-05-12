package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.exception.CJobException;

/**
 * 错误的任务状态,当前任务状态不符合预期
 * @author cocofhu
 */
public class CJobStatusException extends CJobException {
    private final CExecutor currentExecutor;
    public CJobStatusException(CExecutor executor, String msg) {
        super(msg);
        this.currentExecutor = executor;
    }

    public CExecutor getCurrentExecutor() {
        return currentExecutor;
    }
}