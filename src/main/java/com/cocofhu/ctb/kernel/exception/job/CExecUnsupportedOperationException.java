package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 不支持的操作
 * @author cocofhu
 */
public class CExecUnsupportedOperationException extends CBeanException {
    public CExecUnsupportedOperationException(String msg) {
        super(msg);
    }
}
