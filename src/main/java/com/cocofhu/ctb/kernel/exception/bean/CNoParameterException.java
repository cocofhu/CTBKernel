package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 空的参数对象
 * @author cocofhu
 */
public class CNoParameterException extends CBeanException {
    public CNoParameterException(String msg) {
        super(msg);
    }

}
