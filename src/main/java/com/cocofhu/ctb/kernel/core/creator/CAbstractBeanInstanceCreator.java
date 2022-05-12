package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.aware.CConfigAware;
import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.exec.CNoConstructorResolverException;
import com.cocofhu.ctb.kernel.exception.exec.CNoSuchConstructorException;


import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author cocofhu
 */
public abstract class CAbstractBeanInstanceCreator implements CBeanInstanceCreator, CBeanFactoryAware, CConfigAware {

    protected Queue<CConstructorResolver> resolvers;
    protected CBeanFactory beanFactory;
    protected CConfig context;

    @Override
    public void setBeanFactory(CBeanFactory beanFactory) {
        this.beanFactory = beanFactory;
    }


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
            throw new CNoConstructorResolverException("ConstructorResolver set was not set.");
        }
        CExecutableWrapper ctorWrapper = null;
        for (CConstructorResolver resolver : resolvers) {
            ctorWrapper = resolver.resolveConstructor(beanDefinition, config, dataSet);
            if (ctorWrapper != null) break;
        }

        if (ctorWrapper == null) {
            throw new CNoSuchConstructorException("No constructor was found for " + beanDefinition.getBeanClassName() + ".");
        }
        return ctorWrapper;
    }

    @Override
    public void setCTBContext(CConfig context) {
        this.context = context;
    }
}
