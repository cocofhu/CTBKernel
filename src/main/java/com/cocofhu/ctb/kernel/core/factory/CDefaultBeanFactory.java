package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.aware.CBeanNameAware;
import com.cocofhu.ctb.kernel.core.aware.CBeanScopeAware;
import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.exception.*;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.locks.ReentrantLock;


/**
 * 简化的BeanFactory
 * 一、BeanDefinitionReader
 * 二、Bean的生命周期
 * 1、使用指定的构造函数创建Bean
 * 2、创建代理(如果有)
 * 3、调用所有的Aware方法
 * 4、依赖注入
 * 5、调用init-method
 *
 * @author cocofhu
 */
public class CDefaultBeanFactory implements CBeanFactory {

    private final ReentrantLock contextLock = new ReentrantLock();

    private final CBeanDefinitionResolver beanDefinitionResolver;
    private final CBeanInstanceCreator beanCreator;
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    private final Map<String, CBeanDefinition> beanDefinitionsForName = new ConcurrentHashMap<>(256);

//    protected volatile boolean factoryRefreshFinished = false;

    protected void registerBeanDefinition(CBeanDefinition beanDefinition) {
        if (beanDefinition == null) {
            throw new CBadBeanDefinitionException("bean definition is null.");
        }
        beanDefinitionsForName.put(beanDefinition.getBeanName(), beanDefinition);
    }


    public CDefaultBeanFactory() {
        this(new CDefaultBeanInstanceCreator(), ArrayList::new, new CDefaultConstructorResolver());
    }


    public CDefaultBeanFactory(CBeanInstanceCreator beanCreator, CBeanDefinitionResolver beanDefinitionResolver, CConstructorResolver... ctorResolvers) {
        this.beanCreator = beanCreator;
        awareCallBack(this.beanCreator, null);

        this.beanDefinitionResolver = beanDefinitionResolver;
        awareCallBack(this.beanDefinitionResolver, null);

        for (CConstructorResolver ctorResolver : ctorResolvers) {
            awareCallBack(ctorResolver, null);
            beanCreator.registerConstructorResolvers(ctorResolver);
        }

        refresh();
    }

    protected void refresh() {



        // LOCK!
        contextLock.lock();


        singletonObjects.clear();
        beanDefinitionsForName.clear();

        List<CBeanDefinition> beanDefinitions = beanDefinitionResolver.resolveAll();

        for (CBeanDefinition bd : beanDefinitions) {
            registerBeanDefinition(bd);
        }


        // initiating singleton beans
        beanDefinitionsForName.forEach((s, b) -> {
            if (b.isSingleton()) {
                getBean(s);
            }
        });

        // Aware Interface Callback
        beanDefinitionsForName.forEach((s, b) -> {
            if (b.isSingleton()) {
                awareCallBack(getBean(s), b);
            }
        });

        contextLock.unlock();
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
            String[] beanNamesFound = beanDefinitionsForName.entrySet().stream()
                    .filter(entry -> entry.getValue().getBeanClassName().equals(requiredType.getName()))
                    .map(Map.Entry::getKey).toArray(String[]::new);
            if (beanNamesFound.length == 0) {
                throw new CNoSuchBeanDefinitionException(requiredType.getName() + " of bean was not found by type.");
            }
            if (beanNamesFound.length != 1) {
                throw new CNoUniqueBeanDefinitionException(beanNamesFound);
            }
            beanDefinition = beanDefinitionsForName.get(beanNamesFound[0]);
            // 这里如果按照规范注册BeanDefinition，这里不会为空
            if (beanDefinition == null) {
                throw new CNoSuchBeanDefinitionException("beanDefinition is null,  type:" + requiredType.getName());
            }
        } else {
            throw new CNoSuchBeanDefinitionException("can not resolve beanDefinition,both name and type are null.");
        }
        return beanDefinition;
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
    protected <T> T doGetBean(String name, Class<T> requiredType) {


        // Find BeanDefinition
        CBeanDefinition beanDefinition = doGetSingleBeanDefinition(name, requiredType);

        // Get instance of bean
        T beanInstance;
        if (beanDefinition.isSingleton()) {
            beanInstance = doGetSingletonBean(beanDefinition);
        } else if (beanDefinition.isPrototype()) {
            // Prototype
            beanInstance = beanCreator.newInstance(beanDefinition, requiredType);
            awareCallBack(beanInstance, beanDefinition);
            // Call Init Method

        } else {
            throw new CInstantiationException("instantiating bean for " + beanDefinition.getBeanClassName() + " unsupported.");
        }


        return beanInstance;
    }

    @SuppressWarnings("unchecked")
    private <T> T doGetSingletonBean(CBeanDefinition beanDefinition) {
        Object obj = singletonObjects.get(beanDefinition.getBeanName());
        if (obj == null) {
            obj = beanCreator.newInstance(beanDefinition);
            singletonObjects.put(beanDefinition.getBeanName(), obj);
        }
        return (T) obj;
    }

    private void awareCallBack(Object obj, CBeanDefinition beanDefinition) {
        if (obj instanceof CBeanFactoryAware) {
            ((CBeanFactoryAware) obj).setBeanFactory(this);
        }
        if (obj instanceof CBeanNameAware && beanDefinition != null) {
            ((CBeanNameAware) obj).setBeanName(beanDefinition.getBeanName());
        }
        if (obj instanceof CBeanScopeAware && beanDefinition != null) {
            ((CBeanScopeAware) obj).setScope(beanDefinition.isSingleton() ? CBeanDefinition.CBeanScope.SINGLETON : CBeanDefinition.CBeanScope.PROTOTYPE);
        }
    }

    @Override
    public Object getBean(String name) throws CBeansException {
        return doGetBean(name, null);
    }

    @Override
    public <T> T getBean(String name, Class<T> requiredType) throws CBeansException {
        return doGetBean(name, requiredType);
    }

    @Override
    public <T> T getBean(Class<T> requiredType) throws CBeansException {
        return doGetBean(null, requiredType);
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
}
