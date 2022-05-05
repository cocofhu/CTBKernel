package com.cocofhu.ctb.kernel.exception;

/**
 * 错误的CTBContext
 * @author cocofhu
 */
public class CBadCTBContextException extends CNestedRuntimeException {
    public CBadCTBContextException(String msg) {
        super(msg);
    }
}
