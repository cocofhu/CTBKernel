package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * 方法定义出现问题，无法通过CExecutorMethod找到相应的方法
 * @author cocofhu
 */
public class CExecBadMethodException extends CExecException {
    private final CExecutorMethod executorMethod;
    public CExecBadMethodException(CExecutorMethod executorMethod, String msg) {
        super(msg);
        this.executorMethod = executorMethod;
    }

    public CExecutorMethod getExecutorMethod() {
        return executorMethod;
    }
}
