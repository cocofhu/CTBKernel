package com.cocofhu.ctb.kernel.exception.bean;


import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.exception.CBeanException;

import java.util.Arrays;

/**
 * BeanDefinition 不唯一
 * @author cocofhu
 */
public class CNoUniqueBeanDefinitionException extends CBeanException {
    private final CBeanDefinition[] beansFound;
    public CNoUniqueBeanDefinitionException(CBeanDefinition[] beansFound) {
        super("expected single matching bean but found " + beansFound.length + ": "
                + Arrays.toString(Arrays.stream(beansFound).map(CBeanDefinition::getBeanName).toArray(String[]::new)));
        this.beansFound = beansFound;
    }
    public int getNumberOfBeansFound() {
        return this.beansFound.length;
    }

    public CBeanDefinition[] getBeansFound() {
        return beansFound;
    }
}
