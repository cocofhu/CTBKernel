package com.cocofhu.ctb.kernel.exception;


import java.util.Arrays;

public class CNoUniqueBeanDefinitionException extends CNestedRuntimeException {
    private final String[] beanNamesFound;
    public CNoUniqueBeanDefinitionException(String[] beanNamesFound) {
        super("expected single matching bean but found " + beanNamesFound.length + ": " + Arrays.toString(beanNamesFound));
        this.beanNamesFound = beanNamesFound;
    }
    public int getNumberOfBeansFound() {
        return this.beanNamesFound.length;
    }

    public String[] getBeanNamesFound() {
        return this.beanNamesFound;
    }
}
