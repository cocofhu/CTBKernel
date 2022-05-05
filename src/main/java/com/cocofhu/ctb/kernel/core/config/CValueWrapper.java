package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.resolver.CProcess;

/**
 * @author cocofhu
 */
public class CValueWrapper {
    /**
     * 该值是由哪一个值处理器处理出来的
     */
    private final CProcess<CParameterWrapper> valueProcess;
    /**
     * 第二个参数用于表示是否处理成功
     */
    private final CTBPair<Object,Boolean> value;
    /**
     * 上下文
     */
    private final CTBContext context;

    private final CParameterWrapper parameterWrapper;

    public CValueWrapper(CProcess<CParameterWrapper> valueProcess, CTBPair<Object, Boolean> value, CTBContext context, CParameterWrapper parameterWrapper) {
        this.valueProcess = valueProcess;
        this.value = value;
        this.context = context;
        this.parameterWrapper = parameterWrapper;
    }


    public CProcess<CParameterWrapper> getValueProcess() {
        return valueProcess;
    }

    public CTBPair<Object, Boolean> getValue() {
        return value;
    }

    public CParameterWrapper getParameterWrapper() {
        return parameterWrapper;
    }

    public CTBContext getContext() {
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
