package com.cocofhu.ctb.kernel.exception;

/**
 * 参数对应的值不唯一
 */
public class CNoUniqueParameterValueException extends CNestedRuntimeException{
    public CNoUniqueParameterValueException(String msg) {
        super(msg);
    }
}
