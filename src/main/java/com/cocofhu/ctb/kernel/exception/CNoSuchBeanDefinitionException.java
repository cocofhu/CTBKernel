package com.cocofhu.ctb.kernel.exception;

import com.cocofhu.ctb.kernel.exception.CNestedRuntimeException;

public class CNoSuchBeanDefinitionException extends CNestedRuntimeException {
    public CNoSuchBeanDefinitionException(String msg) {
        super(msg);
    }
}
