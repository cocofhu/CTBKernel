package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CPair;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 空参数值
 * @author cocofhu
 */
public class CNoValueException extends CBeanException {
    private final CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> valueProcess;
    public CNoValueException(CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> valueProcess) {
        super("empty parameter value.");
        this.valueProcess = valueProcess;
    }

    public CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> getValueProcess() {
        return valueProcess;
    }
}
