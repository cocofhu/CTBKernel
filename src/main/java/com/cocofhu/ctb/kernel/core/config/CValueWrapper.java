package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.resolver.CProcess;

public class CValueWrapper {
    /**
     * 该值是由哪一个值处理器处理出来的
     */
    private final CProcess valueProcess;
    /**
     * 第二个参数用于表示是否处理成功
     */
    private final CTBPair<Object,Boolean> value;
    /**
     * 上下文
     */
    private final CTBContext context;

    public CValueWrapper(CProcess valueProcess, CTBPair<Object, Boolean> value, CTBContext context) {
        this.valueProcess = valueProcess;
        this.value = value;
        this.context = context;
    }


    public CProcess getValueProcess() {
        return valueProcess;
    }

    public CTBPair<Object, Boolean> getValue() {
        return value;
    }
}
