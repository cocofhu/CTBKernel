package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 没有Bean工厂
 * @author cocofhu
 */
public class CNoBeanFactoryException extends CBeanException {
    public CNoBeanFactoryException(String msg) {
        super(msg);
    }
}
