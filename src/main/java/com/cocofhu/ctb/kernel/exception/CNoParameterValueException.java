package com.cocofhu.ctb.kernel.exception;

/**
 * 没有找到任何参数对应的值
 */
public class CNoParameterValueException extends CNestedRuntimeException{
    public CNoParameterValueException(String msg) {
        super(msg);
    }
}
