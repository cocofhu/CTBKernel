package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import com.cocofhu.ctb.kernel.exception.exec.CExecStatusException;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;

/**
 * @author cocofhu
 */
public abstract class CAbstractExecutor implements CExecutor {

    private volatile Status status;

    protected final CDefaultExecutionRuntime executionRuntime;
    protected final CExecutorDefinition executorDefinition;
    protected final CConfig config;


    /**
     * @param executionRuntime      执行器的上下文，用于存放执行过程中的参数
     * @param config                BeanFactory的上下文，用于获得框架的支持
     * @param executorDefinition    任务定义
     */
    protected CAbstractExecutor(CDefaultExecutionRuntime executionRuntime, CExecutorDefinition executorDefinition, CConfig config) {
        this.executionRuntime = executionRuntime;
        if(executorDefinition != null){
            this.executorDefinition = (CExecutorDefinition) executorDefinition.cloneSelf();
        }else{
            this.executorDefinition = null;
        }
        this.config = config;
        this.status = Status.Ready;
    }

    @Override
    public Object getReturnVal() {
        if (getStatus() != Status.Stop) {
            throw new CExecStatusException(this, "executor not executed successfully.");
        }
        return executionRuntime.getReturnVal();
    }

    @Override
    public Throwable getThrowable() {
        if (getStatus() != Status.Exception) {
            throw new CExecStatusException(this, "executor has not encountered an exception.");
        }
        return executionRuntime.getException();
    }

    @Override
    public boolean isIgnoreException() {
        return executorDefinition != null && executorDefinition.isIgnoreException();
    }

    @Override
    public boolean isExceptionInContext() {
        return executionRuntime.hasException();
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
