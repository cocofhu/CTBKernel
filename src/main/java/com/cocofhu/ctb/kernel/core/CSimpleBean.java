package com.cocofhu.ctb.kernel.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;

/**
 * 简易的JavaBean实现
 * 1、只支持一个无参的构造函数
 */
public class CSimpleBean implements ICBeanBehavior{

    protected Class<?> clazz;
    protected Constructor<?> constructor;
    protected final BeanScope scope;
    protected Object singletonObj;

    public CSimpleBean(Class<?> clazz, BeanScope scope) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        this.clazz = clazz;
        this.scope = scope;
        Constructor<?>[] constructors = clazz.getConstructors();
        if(constructors.length != 1) {
            throw new UnsupportedOperationException("Unsupported：count of constructor not equals to 1.");
        }
        this.constructor = constructors[0];
        if(this.constructor.getParameterCount() != 0){
            throw new UnsupportedOperationException("Unsupported：count of constructor's parameter not equals to 0.");
        }
        if(scope == BeanScope.SINGLETON){
            singletonObj = getInstance();
        }
    }

    public CSimpleBean(Class<?> clazz) throws InvocationTargetException, InstantiationException, IllegalAccessException {
        this(clazz,BeanScope.PROTOTYPE);
    }

    @Override
    public Object getInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException {
        if(singletonObj != null) {
            return singletonObj;
        }
        return getConstructor().newInstance(getInitArgs());
    }

    @Override
    public Constructor<?> getConstructor() {
        return constructor;
    }

    @Override
    public Object[] getInitArgs() {
        return new Object[0];
    }

    @Override
    public BeanScope getScope() {
        return scope;
    }

    @Override
    public Class<?> getBelongingClass() {
        return clazz;
    }


}
