package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.aware.CBeanNameAware;
import com.cocofhu.ctb.kernel.core.aware.CBeanScopeAware;
import com.cocofhu.ctb.kernel.core.aware.CConfigAware;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.CValueResolver;
import com.cocofhu.ctb.kernel.exception.bean.CEmptyBeanDefinitionException;
import com.cocofhu.ctb.kernel.exception.bean.CNoSuchBeanDefinitionException;
import com.cocofhu.ctb.kernel.exception.bean.CNoUniqueBeanDefinitionException;
import com.cocofhu.ctb.kernel.exception.exec.CInstantiationException;


import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;


/**
 * 简化的BeanFactory,非线程安全的
 * <p>
 * 一、实例化过程
 * 1、设置 CBeanInstanceCreator    用于实例化Bean
 * 2、设置 CBeanDefinitionResolver 用于加载所有BeanDefinition
 * 3、设置 CAnnotationProcess[]    用户解析注解
 * 4、设置 CConstructorResolver    用于寻找Bean的构造函数
 * 5、上述1234调用所有的AwareMethod
 * 6、执行 refresh() 加载并初始化BeanFactory
 * <p>
 * 二、Bean的生命周期
 * 1、使用指定的构造函数创建Bean
 * 2、创建代理(如果有)
 * 3、调用所有的Aware方法
 * 4、依赖注入(方法注入)
 * 5、调用init-method
 *
 * @author cocofhu
 */
public class CDefaultBeanFactory implements CBeanFactory {


    // 上下文
    protected final CConfig config;
    // 单例对象
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    // BeanDefinition
    private final Map<String, CBeanDefinition> beanDefinitionsForName = new ConcurrentHashMap<>(256);

    /**
     * 创建一个默认的BeanFactory
     *
     * @param beanCreator            用于创建和实例化Bean
     * @param beanDefinitionResolver 用于加载所有的BeanDefinition
     * @param valueResolver          用于获取参数对应的值，完成依赖注入
     */
    public CDefaultBeanFactory(CBeanInstanceCreator beanCreator, CBeanDefinitionResolver beanDefinitionResolver, CValueResolver valueResolver) {
        this.config = new CConfig(this, beanCreator, beanDefinitionResolver, valueResolver);
        // 回调Aware 用于配置一些全局参数
        awareCallBack(valueResolver, null);
        awareCallBack(beanCreator, null);
        awareCallBack(beanDefinitionResolver, null);
        refresh();
    }

    @Override
    public Object getBean(String name) {
        return doGetBean(name, null, null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) {
        return doGetBean(name, requiredType, null);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) {
        return doGetBean(null, requiredType, null);
    }

    @Override
    public boolean containsBean(String name) {
        return beanDefinitionsForName.containsKey(name);
    }

    @Override
    public boolean isSingleton(String name) throws CNoSuchBeanDefinitionException {
        return doGetSingleBeanDefinition(name, null).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) throws CNoSuchBeanDefinitionException {
        return doGetSingleBeanDefinition(name, null).isPrototype();
    }

    @Override
    public Class<?> getType(String name) throws CNoSuchBeanDefinitionException {
        return doGetSingleBeanDefinition(name, null).getBeanClass();
    }

    @Override
    public CConfig getConfig() {
        return this.config;
    }

    @Override
    public CBeanDefinition getBeanDefinition(String name) {
        return doGetSingleBeanDefinition(name, null);
    }

    @Override
    public CBeanDefinition getBeanDefinition(String name, Class<?> requiredType) {
        return doGetSingleBeanDefinition(name, requiredType);
    }

    @Override
    public CBeanDefinition getBeanDefinition(Class<?> requiredType) {
        return doGetSingleBeanDefinition(null, requiredType);
    }

    @Override
    public Object getBean(CBeanDefinition beanDefinition) {
        return getBean(beanDefinition, null);
    }

    @Override
    public Object getBean(String name, CReadOnlyDataSet<String, Object> dataSet) {
        return doGetBean(name, null, dataSet);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType, CReadOnlyDataSet<String, Object> dataSet) {
        return doGetBean(name, requiredType, dataSet);
    }

    @Override
    public <T> T getBean(Class<T> requiredType, CReadOnlyDataSet<String, Object> dataSet) {
        return doGetBean(null, requiredType, dataSet);
    }

    @Override
    public Object getBean(CBeanDefinition beanDefinition, CReadOnlyDataSet<String, Object> dataSet) {
        if (!beanDefinitionsForName.containsValue(beanDefinition)) {
            throw new CNoSuchBeanDefinitionException("no such bean definition found in bean factory, make sure acquired bean definition by using getBeanDefinition");
        }
        return doGetBeanByBeanDefinition(beanDefinition, dataSet);
    }

    protected void refresh() {


        singletonObjects.clear();
        beanDefinitionsForName.clear();

        List<CBeanDefinition> beanDefinitions = this.config.getBeanDefinitionResolver().resolveAll();
        // 注册获取到的Bean
        for (CBeanDefinition bd : beanDefinitions) {
            registerBeanDefinition(bd);
        }


        // initiating singleton beans
        beanDefinitionsForName.forEach((s, b) -> {
            if (b.isSingleton()) {
                Object o = doCreateBeanInstance(b, null);
                callInitMethods(b, o, null);
                // 放入单例池
                singletonObjects.put(b.getBeanName(), o);
            }
        });

        // Aware Interface Callback
        beanDefinitionsForName.forEach((s, b) -> {
            if (b.isSingleton()) {
                awareCallBack(getBean(s), b);
            }
        });


//        contextLock.unlock();
    }

    protected void registerBeanDefinition(CBeanDefinition beanDefinition) {
        if (beanDefinition == null) {
            throw new CEmptyBeanDefinitionException();
        }
        beanDefinitionsForName.put(beanDefinition.getBeanName(), beanDefinition);
    }

    /**
     * 根据name或者type获得Bean实例对象
     *
     * @param name         Bean的name
     * @param requiredType Bean的类型
     * @throws CNoSuchBeanDefinitionException   没有找到指定的BeanDefinition、BeanDefinition为空或requiredType和name都为空
     * @throws CNoUniqueBeanDefinitionException 查找到的BeanDefinition不唯一
     * @throws CInstantiationException          初始化Bean实例的时候出现错误或BeanScope是不支持的类型
     */

    protected <T> T doGetBean(String name, Class<T> requiredType, CReadOnlyDataSet<String, Object> dataSet) {
        // Find BeanDefinition
        CBeanDefinition beanDefinition = doGetSingleBeanDefinition(name, requiredType);
        return doGetBeanByBeanDefinition(beanDefinition, dataSet);
    }

    /**
     * 根据name或者type获得BeanDefinition
     *
     * @param name         Bean的name
     * @param requiredType Bean的类型
     * @return BeanDefinition对象
     * @throws CNoSuchBeanDefinitionException   没有找到指定的BeanDefinition、BeanDefinition为空或requiredType和name都为空
     * @throws CNoUniqueBeanDefinitionException 查找到的BeanDefinition不唯一
     */
    protected CBeanDefinition doGetSingleBeanDefinition(String name, Class<?> requiredType) {
        CBeanDefinition beanDefinition;
        if (name != null) {
            CBeanDefinition bd = beanDefinitionsForName.get(name);
            if (bd == null) {
                throw new CNoSuchBeanDefinitionException(name + " of bean was not found by name.");
            }
            if (requiredType != null &&
                    !bd.getBeanClassName().equals(requiredType.getName())) {
                throw new CNoSuchBeanDefinitionException(requiredType.getName() + " of bean was not found by type.");
            }
            beanDefinition = bd;
        } else if (requiredType != null) {
            CBeanDefinition[] beansFound = beanDefinitionsForName.values().stream()
                    .filter(cBeanDefinition -> requiredType.isAssignableFrom(cBeanDefinition.getBeanClass())).toArray(CBeanDefinition[]::new);
            if (beansFound.length == 0) {
                throw new CNoSuchBeanDefinitionException(requiredType.getName() + " of bean was not found by type.");
            }
            if (beansFound.length != 1) {
                throw new CNoUniqueBeanDefinitionException(beansFound);
            }
            beanDefinition = beansFound[0];
            // 这里如果按照规范注册BeanDefinition，这里不会为空
            if (beanDefinition == null) {
                throw new CNoSuchBeanDefinitionException("beanDefinition is null,  type:" + requiredType.getName());
            }
        } else {
            throw new CNoSuchBeanDefinitionException("can not resolve beanDefinition,both name and type are null.");
        }
        return beanDefinition;
    }

    private void awareCallBack(Object obj, CBeanDefinition beanDefinition) {
        if (obj instanceof CBeanFactoryAware) {
            ((CBeanFactoryAware) obj).setBeanFactory(this);
        }
        if (obj instanceof CConfigAware) {
            ((CConfigAware) obj).setCTBContext(this.config);
        }
        if (obj instanceof CBeanNameAware && beanDefinition != null) {
            ((CBeanNameAware) obj).setBeanName(beanDefinition.getBeanName());
        }
        if (obj instanceof CBeanScopeAware && beanDefinition != null) {
            ((CBeanScopeAware) obj).setScope(beanDefinition.isSingleton() ? CBeanDefinition.CBeanScope.SINGLETON : CBeanDefinition.CBeanScope.PROTOTYPE);
        }
    }

    private Object doCreateBeanInstance(CBeanDefinition beanDefinition, CReadOnlyDataSet<String, Object> dataSet) {

        return this.config.getInstanceCreator().newInstance(beanDefinition, config, dataSet);
    }

    private void callInitMethods(CBeanDefinition b, Object o, CReadOnlyDataSet<String, Object> dataSet) {
        Method[] methods = b.initMethods();
        for (Method method : methods) {
            CExecutableWrapper executableWrapper = new CExecutableWrapper(method, config, b, dataSet);
            try {
                executableWrapper.execute(o);
            } catch (IllegalAccessException | InvocationTargetException e) {
                throw new CInstantiationException("can not call init-method " + method.getName() + " of bean " + b.getBeanName());
            } catch (InstantiationException e) {
                // It cannot reach here!
                throw new RuntimeException(e);
            }
        }
    }

    @SuppressWarnings("unchecked")
    private <T> T doGetBeanByBeanDefinition(CBeanDefinition beanDefinition, CReadOnlyDataSet<String, Object> dataSet) {
        // Get instance of bean
        Object beanInstance;
        if (beanDefinition.isSingleton()) {
            beanInstance = singletonObjects.get(beanDefinition.getBeanName());
        } else if (beanDefinition.isPrototype()) {
            // Prototype
            beanInstance = doCreateBeanInstance(beanDefinition, dataSet);
            // Call Init Method
            callInitMethods(beanDefinition, beanDefinition, dataSet);

            awareCallBack(beanInstance, beanDefinition);
        } else {
            throw new CInstantiationException("instantiating bean for " + beanDefinition.getBeanClassName() + " unsupported.");
        }

        return (T) beanInstance;
    }

}
