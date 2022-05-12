package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 没有构造器
 * @author cocofhu
 */
public class CNoConstructorException extends CBeanException {
    private final CBeanDefinition beanDefinition;
    public CNoConstructorException(CBeanDefinition beanDefinition) {
        super("No constructor was found for " + beanDefinition.getBeanClassName() + ".");
        this.beanDefinition = beanDefinition;
    }

    public CBeanDefinition getBeanDefinition() {
        return beanDefinition;
    }
}
