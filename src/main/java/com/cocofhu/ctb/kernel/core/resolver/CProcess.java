package com.cocofhu.ctb.kernel.core.resolver;

import com.cocofhu.ctb.kernel.core.config.CPair;
import com.cocofhu.ctb.kernel.core.config.CConfig;

public interface CProcess<T> {
    /**
     * 处理指定的target对象
     */
    CPair<Object,Boolean> process(T target, CConfig config);
}
