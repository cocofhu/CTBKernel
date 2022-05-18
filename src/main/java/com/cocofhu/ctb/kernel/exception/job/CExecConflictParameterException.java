package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * 执行器参数出现冲突(Input,Output,Removal)
 * @author cocofhu
 */
public class CExecConflictParameterException extends CExecException {
    private final CParameterDefinition[] parameters;
    public CExecConflictParameterException(CParameterDefinition[] parameters, String msg) {
        super(msg);
        this.parameters = parameters;
    }

    public CParameterDefinition[] getParameters() {
        return parameters;
    }
}
