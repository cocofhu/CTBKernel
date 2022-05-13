package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * Executable, 当一个Executable为空时将会抛出此异常
 * @author cocofhu
 */
public class CNoExecutableException extends CBeanException {
    public CNoExecutableException(String msg) {
        super(msg);
    }
}
