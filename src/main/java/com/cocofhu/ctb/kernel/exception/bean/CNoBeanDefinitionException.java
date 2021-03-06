package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * BeanDefinition为空, 当一个BeanDefinition为空时将会抛出此异常
 * @author cocofhu
 */
public class CNoBeanDefinitionException extends CBeanException {
    public CNoBeanDefinitionException() {
        super("empty bean definition.");
    }
}
