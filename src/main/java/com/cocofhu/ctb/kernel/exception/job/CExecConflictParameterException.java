package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.core.exec.entity.CJobParam;
import com.cocofhu.ctb.kernel.exception.CJobException;

/**
 * 执行器参数出现冲突(Input,Output,Removal)
 * @author cocofhu
 */
public class CExecConflictParameterException extends CJobException {
    private final CJobParam[] parameters;
    public CExecConflictParameterException(CJobParam[] parameters, String msg) {
        super(msg);
        this.parameters = parameters;
    }

    public CJobParam[] getParameters() {
        return parameters;
    }
}
