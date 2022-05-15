package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;
import com.cocofhu.ctb.kernel.exception.CBeanException;

import java.util.Arrays;
import java.util.List;

/**
 * 参数对应的值不唯一
 * @author cocofhu
 */
public class CNoUniqueParameterValueException extends CBeanException {

    private final CParameterWrapper parameter;
    private final CExecutableWrapper executor;
    private final List<CValueWrapper> valueWrappers;

    public CNoUniqueParameterValueException(CParameterWrapper parameter, CExecutableWrapper executor, List<CValueWrapper> valueWrappers) {
        super("no unique value resolved for parameter :" +
                parameter.getParameter().getName() + " on class constructor or method : "
                + executor.getExecutor().getName() + "(" + Arrays.toString(valueWrappers.stream().map(CValueWrapper::getValue).map(CPair::getFirst).toArray(Object[]::new)) + ")");
        this.valueWrappers = valueWrappers;
        this.executor = executor;
        this.parameter = parameter;
    }

    public CParameterWrapper getParameter() {
        return parameter;
    }

    public CExecutableWrapper getExecutor() {
        return executor;
    }

    public List<CValueWrapper> getValueWrappers() {
        return valueWrappers;
    }
}
