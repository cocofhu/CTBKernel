package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 初始化Bean的时候会出现的异常
 * @author cocofhu
 */
public class CInstantiationException extends CBeanException {
    private final CBeanDefinition beanDefinition;
    public CInstantiationException(String msg, CBeanDefinition beanDefinition) {
        super(msg);
        this.beanDefinition = beanDefinition;
    }

    public CBeanDefinition getBeanDefinition() {
        return beanDefinition;
    }
}
