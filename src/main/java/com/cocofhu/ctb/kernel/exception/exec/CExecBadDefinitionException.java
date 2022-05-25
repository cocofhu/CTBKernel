package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * 空的或者不合法的 CExecutorDefinition
 * @author cocofhu
 */
public class CExecBadDefinitionException extends CExecException {
    public CExecBadDefinitionException(String msg) {
        super(msg);
    }
}
