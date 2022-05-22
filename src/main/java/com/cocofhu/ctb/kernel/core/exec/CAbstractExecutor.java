package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;
import com.cocofhu.ctb.kernel.exception.exec.CExecStatusException;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;

/**
 * @author cocofhu
 */
public abstract class CAbstractExecutor implements CExecutor {

    private volatile Status status;

    protected final CDefaultExecutionRuntime executionRuntime;
    protected final CConfig config;
    protected final boolean ignoreException;

    protected CReadOnlyDataSet<String, Object> attachment;

    /**
     * @param executionRuntime   执行器的上下文，用于存放执行过程中的参数
     * @param config             BeanFactory的上下文，用于获得框架的支持
     * @param ignoreException    是否忽略上一次执行出现的异常
     * @param attachment         附加参数
     */
    protected CAbstractExecutor(CDefaultExecutionRuntime executionRuntime, CConfig config, boolean ignoreException, CReadOnlyDataSet<String, Object> attachment) {
        this.executionRuntime = executionRuntime;
        this.config = config;
        this.ignoreException = ignoreException;
        this.attachment = attachment;
        this.status = Status.NotReady;

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
        return (Throwable) executionRuntime.getException();
    }

    @Override
    public boolean isIgnoreException() {
        return ignoreException;
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
    public void setAttachment(CReadOnlyDataSet<String, Object> attachment) {
        this.attachment = attachment;
    }
}
