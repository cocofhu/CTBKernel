package com.cocofhu.ctb.kernel.core;

import com.cocofhu.ctb.kernel.NestedRuntimeException;

public class CNoSuchBeanDefinitionException extends NestedRuntimeException {
    public CNoSuchBeanDefinitionException(String msg) {
        super(msg);
    }

    public CNoSuchBeanDefinitionException(String msg, Throwable cause) {
        super(msg, cause);
    }
}
