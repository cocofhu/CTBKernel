package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 初始化Bean的时候会出现的异常
 * @author cocofhu
 */
public class CInstantiationException extends CBeanException {
    public CInstantiationException(String msg) {
        super(msg);
    }
}
