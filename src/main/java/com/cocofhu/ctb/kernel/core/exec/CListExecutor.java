package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;

/**
 * @author cocofhu
 */
public class CListExecutor extends CAbstractExecutor {
    
    
    private final CExecutor[] executors;
    private final boolean ignoreException;

    public CListExecutor(CConfig config, boolean ignoreException, CExecutorDefinition definition, CExecutor... executors) {
        super(definition, config);
        this.executors = executors;
        this.ignoreException = ignoreException;
    }


    @Override
    public void run(CExecutionRuntime runtime) {

        for (CExecutor executor : executors) {
            runtime.start(executorDefinition.getAttachment(), CExecutionRuntime.CExecutorRuntimeType.ARGS_COPY, this);
            executor.setStatus(Status.Ready);
            executor.run(runtime);
        }
        setStatus(Status.Stop);
    }

    @Override
    public boolean isIgnoreException() {
        return ignoreException;
    }

}
