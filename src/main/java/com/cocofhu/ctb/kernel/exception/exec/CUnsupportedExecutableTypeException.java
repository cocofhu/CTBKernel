package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 不支持的Executable类型
 * @author cocofhu
 */
public class CUnsupportedExecutableTypeException extends CBeanException {
    public CUnsupportedExecutableTypeException(String msg) {
        super(msg);
    }
}
