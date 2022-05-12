package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CPair;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 空的参数对象
 * @author cocofhu
 */
public class CEmptyParameterException extends CBeanException {
    public CEmptyParameterException(String msg) {
        super(msg);
    }

}
