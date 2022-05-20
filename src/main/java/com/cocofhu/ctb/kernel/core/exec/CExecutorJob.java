package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;

import java.util.Arrays;

/**
 * @author cocofhu
 */
public class CExecutorJob extends CAbstractExecutor {
    
    
    private final CExecutor[] executors;

    public CExecutorJob(CDefaultExecutionRuntime executionRuntime, CConfig beanFactoryContext, boolean ignoreException, CExecutor... executors) {
        super(executionRuntime, beanFactoryContext, ignoreException, null);
        this.executors = executors;
    }


    @Override
    public void run() {

        for (CExecutor executor : executors) {
            executor.setStatus(Status.Ready);
            executor.run();
        }

        setStatus(Status.Stop);
    }

    @Override
    public String toString() {
        return "CExecutorJob{" +
                "executors=" + Arrays.toString(executors) +
                ", executorContext=" + executionRuntime +
                ", config=" + config +
                ", ignoreException=" + ignoreException +
                ", attachment=" + attachment +
                '}';
    }
}
