package com.cocofhu.ctb.kernel.exception;

/**
 * 不支持的Executable类型
 * @author cocofhu
 */
public class CUnsupportedExecutableTypeException extends CNestedRuntimeException {
    public CUnsupportedExecutableTypeException(String msg) {
        super(msg);
    }
}
