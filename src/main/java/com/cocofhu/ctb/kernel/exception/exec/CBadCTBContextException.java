package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 错误的CTBContext
 * @author cocofhu
 */
public class CBadCTBContextException extends CBeanException {
    public CBadCTBContextException(String msg) {
        super(msg);
    }
}
