package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.aware.CTBContextAware;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;


import java.util.PriorityQueue;
import java.util.Queue;

/**
 * @author cocofhu
 */
public abstract class CAbstractBeanInstanceCreator implements CBeanInstanceCreator, CBeanFactoryAware, CTBContextAware {

    protected Queue<CConstructorResolver> resolvers;
    protected CBeanFactory beanFactory;
    protected CTBContext context;

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
    public void setCTBContext(CTBContext context) {
        this.context = context;
    }
}
