package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;

import java.util.HashMap;
import java.util.Map;

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

    public CExecutor newExecutor(String beanName, String methodName, boolean ignoreException, CTBPair<String,Object>... attachments){
        Map<String,Object> attach = new HashMap<>();
        for (CTBPair<String, Object> attachment : attachments) {
            attach.put(attachment.getFirst(), attachment.getSecond());
        }
        return new CSimpleExecutor(executorContext,beanFactoryContext,new CExecutorMethod(beanName,null,methodName,null),ignoreException, attach);
    }
    public CExecutor newExecutor(String beanName,String methodName,CTBPair<String,Object>... attachments){
        return newExecutor(beanName,methodName,false,attachments);
    }
}
