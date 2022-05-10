package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;

/**
 * @author cocofhu
 */
public class CExecutorJob extends CAbstractExecutor {
    
    
    private final CExecutor[] executors;

    public CExecutorJob(CExecutorContext executorContext, CConfig beanFactoryContext, boolean ignoreException, CExecutor... executors) {
        super(executorContext, beanFactoryContext, ignoreException, null);
        this.executors = executors;
    }


    @Override
    public void run() {

        for (int i = 0; i < executors.length; i++) {
            CExecutor executor = executors[i];
            if(i == 0){
                executor.setAttachment(attachment);
            }
            executor.setStatus(Status.Ready);
            executor.run();
        }

        setStatus(Status.Stop);
    }
}
