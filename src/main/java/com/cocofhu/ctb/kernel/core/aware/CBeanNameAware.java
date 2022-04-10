package com.cocofhu.ctb.kernel.core.aware;

import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;

public interface CBeanNameAware {
    /**
     * 设置BeanFactory对象，用于构造器注入
     */
    void setBeanName(String name);
}
