package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CTBContext;

/**
 * @author cocofhu
 */
public class CExecutorBuilder {
    protected final CExecutorContext executorContext;
    protected final CTBContext beanFactoryContext;

    public CExecutorBuilder(CExecutorContext executorContext, CTBContext beanFactoryContext) {
        this.executorContext = executorContext;
        this.beanFactoryContext = beanFactoryContext;
    }

    public CExecutor newExecutor(String beanName,String methodName,boolean ignoreException){
        return new CSimpleExecutor(executorContext,beanFactoryContext,new CExecutorMethod(beanName,null,methodName,null),ignoreException);
    }
    public CExecutor newExecutor(String beanName,String methodName){
        return newExecutor(beanName,methodName,false);
    }
}
