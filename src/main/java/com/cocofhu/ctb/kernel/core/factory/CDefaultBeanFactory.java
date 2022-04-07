package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.Startup;
import com.cocofhu.ctb.kernel.core.config.CConstructorWrapper;
import com.cocofhu.ctb.kernel.exception.*;
import com.cocofhu.ctb.kernel.core.config.CAbstractBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CDefaultBeanFactory implements CBeanFactory {

    private final CBeanInstanceCreator beanCreator;
    private final Map<String, Object> singletonObjects = new ConcurrentHashMap<>(256);
    private final Map<String, CBeanDefinition> beanDefinitionsForName = new ConcurrentHashMap<>(256);



    public CDefaultBeanFactory() {
        this.beanCreator = new CDefaultBeanInstanceCreator();
        beanCreator.setBeanFactory(this);
        // Register default ctor resolver
        beanCreator.registerConstructorResolvers(bd->{
            if(bd == null){
                throw new CBadBeanDefinitionException("empty bean definition.");
            }
            Class<?> clazz = bd.getBeanClass();
            try {
                return new CConstructorWrapper(clazz.getConstructor(),new Object[0]);
            } catch (NoSuchMethodException e) {
                return null;
            }
        });

        beanDefinitionsForName.put("ABC", new CAbstractBeanDefinition(Startup.class) {
            public String getBeanName() {
                return "ABC";
            }

            @Override
            public Map<String, Object> resourceBundles() {
                return null;
            }

        });
        beanDefinitionsForName.put("BCD", new CAbstractBeanDefinition(Startup.class) {
            public String getBeanName() {
                return "BCD";
            }

            @Override
            public Map<String, Object> resourceBundles() {
                return null;
            }

        });

        //
        beanDefinitionsForName.forEach((s,b)->{if(b.isSingleton())getBean(s);});
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


        T beanInstance = null;

        if (beanDefinition.isSingleton()) {
            beanInstance = doGetSingletonBean(beanDefinition);
        } else if (beanDefinition.isPrototype()) {
            // just for warning
            beanInstance = beanCreator.newInstance(beanDefinition,requiredType);
        } else {
            throw new CInstantiationException("instantiating bean for "+beanDefinition.getBeanClassName()+" unsupported.");
        }

        // Aware Interface Callback

        // Call Init Method





        return (T) beanInstance;
    }

    @SuppressWarnings("unchecked")
    private <T> T doGetSingletonBean(CBeanDefinition beanDefinition) {
        Object obj = singletonObjects.get(beanDefinition.getBeanName());
        if (obj == null) {
            beanCreator.newInstance(beanDefinition);
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
