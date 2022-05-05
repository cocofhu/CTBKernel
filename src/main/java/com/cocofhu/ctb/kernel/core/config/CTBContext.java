package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.CValueResolver;

import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cocofhu
 */
public class CTBContext {
    private final CBeanFactory beanFactory;
    private final CBeanInstanceCreator instanceCreator;
    private final CBeanDefinitionResolver beanDefinitionResolver;
    private final CValueResolver valueResolver;

    private final Map<String,Object> executionResourceBundles;

    private CTBContext(CBeanFactory beanFactory, CBeanInstanceCreator instanceCreator, CBeanDefinitionResolver beanDefinitionResolver, CValueResolver valueResolver, Map<String, Object> executionResourceBundles) {
        this.beanFactory = beanFactory;
        this.instanceCreator = instanceCreator;
        this.beanDefinitionResolver = beanDefinitionResolver;
        this.valueResolver = valueResolver;
        this.executionResourceBundles = executionResourceBundles;
    }

    public CTBContext(CBeanFactory beanFactory, CBeanInstanceCreator instanceCreator, CBeanDefinitionResolver beanDefinitionResolver, CValueResolver valueResolver) {
        this(beanFactory,instanceCreator,beanDefinitionResolver,valueResolver,null);
    }



    public CValueResolver getValueResolver() {
        return valueResolver;
    }

    public CBeanFactory getBeanFactory() {
        return beanFactory;
    }

    public CBeanDefinitionResolver getBeanDefinitionResolver() {
        return beanDefinitionResolver;
    }

    public CBeanInstanceCreator getInstanceCreator() {
        return instanceCreator;
    }

    public CTBContext newCTBContext(){
        return new CTBContext(beanFactory,instanceCreator,beanDefinitionResolver,valueResolver,new HashMap<>());
    }

    public Map<String, Object> getExecutionResourceBundles() {
        return executionResourceBundles;
    }

    public Object get(String key) {
        return executionResourceBundles.get(key);
    }

    public Object put(String key, Object val) {
        return executionResourceBundles.put(key, val);
    }

}
