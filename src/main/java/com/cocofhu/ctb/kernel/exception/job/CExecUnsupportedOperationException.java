package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * 不支持的操作
 * @author cocofhu
 */
public class CExecUnsupportedOperationException extends CExecException {
    public CExecUnsupportedOperationException(String msg) {
        super(msg);
    }
}
