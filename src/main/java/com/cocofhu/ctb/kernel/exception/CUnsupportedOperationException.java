package com.cocofhu.ctb.kernel.exception;

/**
 * 不支持的操作
 * @author cocofhu
 */
public class CUnsupportedOperationException extends CNestedRuntimeException {
    public CUnsupportedOperationException(String msg) {
        super(msg);
    }
}
