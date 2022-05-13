package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.bean.CNoConstructorResolverException;
import com.cocofhu.ctb.kernel.exception.bean.CNoConstructorException;


import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author cocofhu
 */
public abstract class CAbstractBeanInstanceCreator implements CBeanInstanceCreator {

    protected Queue<CConstructorResolver> resolvers;


    @Override
    public void registerConstructorResolvers(CConstructorResolver resolver) {
        if(resolvers == null){
            resolvers = new PriorityQueue<>(4);
        }
        resolvers.add(resolver);
    }

    @Override
    public CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CConfig config, CReadOnlyDataSet<String, Object> dataSet) {
        if (resolvers == null || resolvers.size() == 0) {
            throw new CNoConstructorResolverException();
        }
        CExecutableWrapper ctorWrapper = null;
        for (CConstructorResolver resolver : resolvers) {
            ctorWrapper = resolver.resolveConstructor(beanDefinition, config, dataSet);
            if (ctorWrapper != null) break;
        }

        if (ctorWrapper == null) {
            throw new CNoConstructorException(beanDefinition);
        }
        return ctorWrapper;
    }

}
