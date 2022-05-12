package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 没有构造器
 * @author cocofhu
 */
public class CNoSuchConstructorException extends CBeanException {
    public CNoSuchConstructorException(String msg) {
        super(msg);
    }
}
