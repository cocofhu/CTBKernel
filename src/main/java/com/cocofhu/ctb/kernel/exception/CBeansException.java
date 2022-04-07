package com.cocofhu.ctb.kernel.exception;

import com.cocofhu.ctb.kernel.exception.CNestedRuntimeException;

public class CBeansException extends CNestedRuntimeException {
    public CBeansException(String msg) {
        super(msg);
    }

    public CBeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
