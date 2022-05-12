package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 没有找到任何参数对应的值
 * @author cocofhu
 */
public class CNoParameterValueException extends CBeanException {
    public CNoParameterValueException(String msg) {
        super(msg);
    }
}
