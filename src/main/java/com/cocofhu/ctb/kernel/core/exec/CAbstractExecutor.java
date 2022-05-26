package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import com.cocofhu.ctb.kernel.exception.exec.CExecStatusException;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;

/**
 * @author cocofhu
 */
public abstract class CAbstractExecutor implements CExecutor {

    private volatile Status status;

    protected final CExecutorDefinition executorDefinition;
    protected final CConfig config;


    /**
     * @param config             全局配置，用于获得框架的支持
     * @param executorDefinition 任务定义
     */
    protected CAbstractExecutor(CExecutorDefinition executorDefinition, CConfig config) {
        if (executorDefinition != null) {
            this.executorDefinition = (CExecutorDefinition) executorDefinition.cloneSelf();
        } else {
            this.executorDefinition = null;
        }
        this.config = config;
        this.status = Status.Ready;
    }

    @Override
    public boolean isIgnoreException() {
        return executorDefinition != null && executorDefinition.isIgnoreException();
    }

    @Override
    public boolean isExecutedSuccessfully() {
        return status == Status.Stop;
    }

    @Override
    public Status getStatus() {
        return status;
    }

    @Override
    public void setStatus(Status status) {
        this.status = status;
    }

    @Override
    public void saveState() {
        throw new CExecUnsupportedOperationException("save state unsupported.");
    }

    @Override
    public void loadState() {
        throw new CExecUnsupportedOperationException("load state unsupported.");
    }

    @Override
    public CExecutorDefinition getExecutorDefinition() {
        return executorDefinition;
    }


}
