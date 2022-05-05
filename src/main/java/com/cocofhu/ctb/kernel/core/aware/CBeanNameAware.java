package com.cocofhu.ctb.kernel.core.aware;


/**
 * @author cocofhu
 */
public interface CBeanNameAware {
    /**
     * 设置BeanFactory对象，用于构造器注入
     */
    void setBeanName(String name);
}
