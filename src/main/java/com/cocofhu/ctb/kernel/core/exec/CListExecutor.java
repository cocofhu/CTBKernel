package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

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
        CExecutionRuntime newRuntime = runtime.start(executorDefinition.getAttachment(), CExecutionRuntime.CExecutorRuntimeType.LIST, this);
        for (CExecutor executor : executors) {
            executor.setStatus(Status.Ready);
            executor.run(newRuntime);
        }
        setStatus(Status.Stop);
        newRuntime.finish();
    }

    @Override
    public boolean isIgnoreException() {
        return ignoreException;
    }

}
