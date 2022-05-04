package com.cocofhu.ctb.kernel.core.resolver;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CTBContext;

public interface CProcess<T> {
    /**
     * 处理指定的target对象
     */
    CTBPair<Object,Boolean> process(T target, CTBContext context);
}
