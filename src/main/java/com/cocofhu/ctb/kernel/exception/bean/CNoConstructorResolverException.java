package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 创建BeanFactory没有指定ConstructorResolver
 * @author cocofhu
 */
public class CNoConstructorResolverException extends CBeanException {
    public CNoConstructorResolverException() {
        super("constructor resolver set was not set.");
    }
}
