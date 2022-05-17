package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.entity.CExecParam;
import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * 执行器参数出现冲突(Input,Output,Removal)
 * @author cocofhu
 */
public class CExecConflictParameterException extends CExecException {
    private final CExecParam[] parameters;
    public CExecConflictParameterException(CExecParam[] parameters, String msg) {
        super(msg);
        this.parameters = parameters;
    }

    public CExecParam[] getParameters() {
        return parameters;
    }
}
