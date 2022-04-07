package com.cocofhu.ctb.kernel.core;

import com.cocofhu.ctb.kernel.NestedRuntimeException;

public class CBeansException extends NestedRuntimeException {
    public CBeansException(String msg) {
        super(msg);
    }

    public CBeansException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
