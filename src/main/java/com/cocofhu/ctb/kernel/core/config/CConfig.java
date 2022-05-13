package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.creator.CBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.CValueResolver;
import com.cocofhu.ctb.kernel.exception.bean.CBadConfigException;

/**
 * @author cocofhu
 */
public class CConfig {
    private final CBeanFactory beanFactory;
    private final CBeanInstanceCreator instanceCreator;
    private final CBeanDefinitionResolver beanDefinitionResolver;
    private final CValueResolver valueResolver;


    public CConfig(CBeanFactory beanFactory, CBeanInstanceCreator instanceCreator, CBeanDefinitionResolver beanDefinitionResolver, CValueResolver valueResolver) {
        this.beanFactory = beanFactory;
        this.instanceCreator = instanceCreator;
        this.beanDefinitionResolver = beanDefinitionResolver;
        this.valueResolver = valueResolver;
        if(beanFactory == null){
            throw new CBadConfigException(this, "can not create config object, bean factory is null.");
        }
        if(instanceCreator == null){
            throw new CBadConfigException(this, "can not create config object, instance creator is null.");
        }
        if(beanDefinitionResolver == null){
            throw new CBadConfigException(this, "can not create config object, bean definition resolver is null.");
        }
        if(valueResolver == null){
            throw new CBadConfigException(this, "can not create config object, value resolver is null.");
        }
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


}
