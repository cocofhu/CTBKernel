package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.config.CPair;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cocofhu
 */
public class CExecutorBuilder {
    protected final CExecutorContext executorContext;
    protected final CConfig beanFactoryContext;

    public CExecutorBuilder(CExecutorContext executorContext, CConfig beanFactoryContext) {
        this.executorContext = executorContext;
        this.beanFactoryContext = beanFactoryContext;
    }

    public CExecutor newExecutor(String beanName, String methodName, boolean ignoreException, CPair<String,Object>... attachments){
//        Map<String,Object> attach = new HashMap<>();
//        for (CPair<String, Object> attachment : attachments) {
//            attach.put(attachment.getFirst(), attachment.getSecond());
//        }
//        return new CSimpleExecutor(executorContext,beanFactoryContext,new CExecutorMethod(beanName,null,methodName,null),ignoreException, attach);
        return null;
    }
    public CExecutor newExecutor(String beanName, String methodName, CPair<String,Object>... attachments){
        return newExecutor(beanName,methodName,false,attachments);
    }
}
