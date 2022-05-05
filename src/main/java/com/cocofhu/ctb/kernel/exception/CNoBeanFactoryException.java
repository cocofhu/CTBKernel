package com.cocofhu.ctb.kernel.exception;

/**
 * 没有Bean工厂
 * @author cocofhu
 */
public class CNoBeanFactoryException extends CNestedRuntimeException{
    public CNoBeanFactoryException(String msg) {
        super(msg);
    }
}
