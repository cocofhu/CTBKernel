package com.cocofhu.ctb.kernel.core;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;


public interface ICBeanBehavior {
    /**
     * Bean的作用域模式
     */
    enum BeanScope{
        /**
         * 单例模式，此模式下的Bean只会创建一个实例
         */
        SINGLETON ,
        /**
         * 原型模式，此模式下的Bean每次获取实例时都会创建一个新的实例
         */
        PROTOTYPE ;

    }

    /**
     * 获得Bean所属的Class对象
     */
    Class<?> getBelongingClass();
    /**
     * 获取JavaBean所对应的实例
     */
    Object getInstance() throws InvocationTargetException, InstantiationException, IllegalAccessException;
    /**
     * 获取创建该Bean的构造函数，通过实现该接口可以动态指定构造函数
     */
    Constructor<?> getConstructor();
    /**
     * 获取创建该Bean的构造函数所需要参数
     */
    Object[] getInitArgs();

    /**
     * 获得该Bean所对应的作用域模式
     */
    BeanScope getScope();
}
