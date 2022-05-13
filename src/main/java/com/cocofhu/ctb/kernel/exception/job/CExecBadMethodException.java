package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.exception.CJobException;

import java.util.Arrays;

/**
 * 方法定义出现问题，无法通过CExecutorMethod找到相应的方法
 * @author cocofhu
 */
public class CExecBadMethodException extends CJobException {
    private final CExecutorMethod executorMethod;
    public CExecBadMethodException(CExecutorMethod executorMethod, String msg) {
        super(msg);
        this.executorMethod = executorMethod;
    }

    public CExecutorMethod getExecutorMethod() {
        return executorMethod;
    }
}
