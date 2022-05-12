package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 没有找到任何参数对应的值
 * @author cocofhu
 */
public class CNoParameterValueException extends CBeanException {
    private final CParameterWrapper parameter;
    private final CExecutableWrapper executor;
    public CNoParameterValueException(CParameterWrapper parameter, CExecutableWrapper executor) {
        super("no value resolved for parameter :" +
                parameter.getParameter().getName() + " on class constructor or method : " + executor.getExecutor().getName());
        this.parameter = parameter;
        this.executor = executor;
    }

    public CParameterWrapper getParameter() {
        return parameter;
    }

    public CExecutableWrapper getExecutor() {
        return executor;
    }
}
