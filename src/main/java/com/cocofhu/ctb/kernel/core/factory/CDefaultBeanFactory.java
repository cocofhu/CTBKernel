package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.Startup;
import com.cocofhu.ctb.kernel.core.CBeansException;
import com.cocofhu.ctb.kernel.core.CNoSuchBeanDefinitionException;
import com.cocofhu.ctb.kernel.core.CNoUniqueBeanDefinitionException;
import com.cocofhu.ctb.kernel.core.config.CAbstractBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConstructorExecutionWrapper;

import java.lang.reflect.Constructor;
import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CDefaultBeanFactory implements CBeanFactory {

    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    private final Map<String, CBeanDefinition> beanDefinitionsForName = new ConcurrentHashMap<>(256);

    {
        beanDefinitionsForName.put("ABC", new CAbstractBeanDefinition(Startup.class) {
            public String getBeanName() {
                return "ABC";
            }

            @Override
            public CConstructorExecutionWrapper resolveConstructor() {
                Constructor<?> constructor = null;
                try {
                    constructor = getBeanClass().getConstructor(Double.class, Double.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return new CConstructorExecutionWrapper(constructor, new Object[]{1.0, 2.0});

            }
        });
        beanDefinitionsForName.put("BCD", new CAbstractBeanDefinition(Startup.class) {
            public String getBeanName() {
                return "BCD";
            }

            @Override
            public CConstructorExecutionWrapper resolveConstructor() {
                Constructor<?> constructor = null;
                try {
                    constructor = getBeanClass().getConstructor(Double.class, Double.class);
                } catch (NoSuchMethodException e) {
                    e.printStackTrace();
                }
                return new CConstructorExecutionWrapper(constructor, new Object[]{1.0, 2.0});

            }
        });
    }

    protected CBeanDefinition doGetSingleBeanDefinition(String name, Class<?> requiredType){
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
            if (beanDefinition == null) {
                throw new CNoSuchBeanDefinitionException("beanDefinition is null,  type:" + requiredType.getName());
            }
        } else {
            throw new CNoSuchBeanDefinitionException("can not resolve beanDefinition,both name and type are null.");
        }
        return beanDefinition;
    }


    protected <T> T doGetBean(String name, Class<T> requiredType) {


        // Find BeanDefinition
        CBeanDefinition beanDefinition = doGetSingleBeanDefinition(name,requiredType);

        // Get instance of bean


        if (beanDefinition.isSingleton()) {
            return doGetSingletonBean(beanDefinition);
        } else if (beanDefinition.isPrototype()) {

        } else {
            // Unsupported Instantiation
        }


        // Call Init Method

        // Aware Interface Callback


//        Class<?> beanClass = beanDefinition.getBeanClass();
//        Constructor<?> constructor = beanDefinition.resolveConstructor().getConstructor();


        return null;
    }

    @SuppressWarnings("unchecked")
    private <T> T doGetSingletonBean(CBeanDefinition beanDefinition) {
        Object obj = singletonObjects.get(beanDefinition.getBeanName());
        if (obj == null) {
            CConstructorExecutionWrapper ctorWrapper = beanDefinition.resolveConstructor();
            try {
                obj = ctorWrapper.getConstructor().newInstance(ctorWrapper.getParameters());
            } catch (InstantiationException | IllegalAccessException | InvocationTargetException e) {
                e.printStackTrace();
            }
            singletonObjects.put(beanDefinition.getBeanName(), obj);
        }
        return (T) obj;
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
        return doGetSingleBeanDefinition(name,null).isSingleton();
    }

    @Override
    public boolean isPrototype(String name) throws CNoSuchBeanDefinitionException {
        return doGetSingleBeanDefinition(name,null).isPrototype();
    }

    @Override
    public Class<?> getType(String name) throws CNoSuchBeanDefinitionException {
        return doGetSingleBeanDefinition(name,null).getBeanClass();
    }
}
