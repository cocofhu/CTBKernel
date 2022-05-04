package com.cocofhu.ctb.kernel.core.factory.exec;

import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;

public class CExecutorBuilder {
    protected final CExecutorContext context;
    protected final CMethodBeanFactory beanFactory;

    public CExecutorBuilder(CExecutorContext context, CMethodBeanFactory beanFactory) {
        this.context = context;
        this.beanFactory = beanFactory;
    }

    public CExecutor newExecutor(String beanName,String methodName,boolean ignoreException){
        return new CSimpleExecutor(context,beanFactory,new CExecutorMethod(beanName,null,methodName,null),ignoreException);
    }
    public CExecutor newExecutor(String beanName,String methodName){
        return newExecutor(beanName,methodName,false);
    }
}
