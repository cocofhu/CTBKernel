package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.anno.param.process.CAnnoProcess;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 空参数值
 * @author cocofhu
 */
public class CNoValueException extends CBeanException {
    private final CAnnoProcess valueProcess;
    public CNoValueException(CAnnoProcess valueProcess) {
        super("empty parameter value.");
        this.valueProcess = valueProcess;
    }

    public CAnnoProcess getValueProcess() {
        return valueProcess;
    }
}
