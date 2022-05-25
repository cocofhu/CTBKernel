package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;

import java.util.Arrays;

/**
 * @author cocofhu
 */
public class CExecutorJob extends CAbstractExecutor {
    
    
    private final CExecutor[] executors;
    private final boolean ignoreException;

    public CExecutorJob(CDefaultExecutionRuntime executionRuntime, CConfig config, boolean ignoreException, CExecutor... executors) {
        super(executionRuntime, null, config);
        this.executors = executors;
        this.ignoreException = ignoreException;
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
    public boolean isIgnoreException() {
        return ignoreException;
    }
}
