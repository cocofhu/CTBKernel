package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.resolver.CProcess;

/**
 * @author cocofhu
 */
public class CValueWrapper {
    /**
     * 该值是由哪一个值处理器处理出来的
     */
    private final CProcess<CPair<CParameterWrapper,CDefaultDefaultReadOnlyDataSet>> valueProcess;
    /**
     * 第二个参数用于表示是否处理成功
     */
    private final CPair<Object,Boolean> value;
    /**
     * 上下文
     */
    private final CConfig context;

    private final CParameterWrapper parameterWrapper;

    public CValueWrapper(CProcess<CPair<CParameterWrapper,CDefaultDefaultReadOnlyDataSet>> valueProcess, CPair<Object, Boolean> value, CConfig context, CParameterWrapper parameterWrapper) {
        this.valueProcess = valueProcess;
        this.value = value;
        this.context = context;
        this.parameterWrapper = parameterWrapper;
    }


    public CProcess<CPair<CParameterWrapper,CDefaultDefaultReadOnlyDataSet>> getValueProcess() {
        return valueProcess;
    }

    public CPair<Object, Boolean> getValue() {
        return value;
    }

    public CParameterWrapper getParameterWrapper() {
        return parameterWrapper;
    }

    public CConfig getContext() {
        return context;
    }

    @Override
    public String toString() {
        return "CValueWrapper{" +
                "valueProcess=" + valueProcess +
                ", value=" + value +
                ", context=" + context +
                ", parameterWrapper=" + parameterWrapper +
                '}';
    }
}
