package com.cocofhu.ctb.kernel.exception;

/**
 * 错误的CTBContext
 */
public class CBadCTBContextException extends CNestedRuntimeException {
    public CBadCTBContextException(String msg) {
        super(msg);
    }
}
