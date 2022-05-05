package com.cocofhu.ctb.kernel.core.config;

import java.lang.annotation.Annotation;

/**
 * @author cocofhu
 */
public abstract class CAbstractDefinition implements CDefinition {



    protected volatile Class<?> beanClass;
    protected final CBeanScope scope;

    public CAbstractDefinition(Class<?> beanClass, CBeanScope scope) {
        this.beanClass = beanClass;
        this.scope = scope;
    }

    public CAbstractDefinition(Class<?> beanClass) {
        this(beanClass,CBeanScope.PROTOTYPE);
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

    @Override
    public Annotation[] getAnnotations() {
        if(beanClass !=null){
            return beanClass.getAnnotations();
        }
        return new Annotation[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        if(beanClass == null){
            return null;
        }
        return beanClass.getAnnotation(clazz);
    }
}
