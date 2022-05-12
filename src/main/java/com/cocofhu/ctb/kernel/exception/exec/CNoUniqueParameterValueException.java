package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 参数对应的值不唯一
 * @author cocofhu
 */
public class CNoUniqueParameterValueException extends CBeanException {
    public CNoUniqueParameterValueException(String msg) {
        super(msg);
    }
}
