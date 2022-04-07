package com.cocofhu.ctb.kernel.core.config;

import java.lang.reflect.Constructor;

public abstract class CAbstractBeanDefinition implements CBeanDefinition{



    protected volatile Class<?> beanClass;
    protected final CBeanScope scope;

    public CAbstractBeanDefinition(Class<?> beanClass, CBeanScope scope) {
        this.beanClass = beanClass;
        this.scope = scope;
    }

    public CAbstractBeanDefinition(Class<?> beanClass) {
        this(beanClass,CBeanScope.SINGLETON);
    }

    @Override
    public CConstructorExecutionWrapper resolveConstructor() {
        try {
            Constructor<?> constructor = beanClass.getConstructor();
            return new CConstructorExecutionWrapper(constructor,new Object[0]);
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        }
        return null;
    }

    @Override
    public String getBeanClassName() {
        return beanClass.getName();
    }

    @Override
    public Class<?> getBeanClass() {
        return beanClass;
    }

    @Override
    public boolean isSingleton() {
        return scope.equals(CBeanScope.SINGLETON);
    }

    @Override
    public boolean isPrototype() {
        return scope.equals(CBeanScope.PROTOTYPE);
    }


}
