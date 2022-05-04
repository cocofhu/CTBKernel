package com.cocofhu.ctb.kernel.core.factory.exec;

import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.exception.CExecutorStatusException;
import com.cocofhu.ctb.kernel.exception.CUnsupportedOperationException;

public abstract class CAbstractExecutor implements CExecutor {

    protected volatile Status status;

    protected final CExecutorContext context;
    protected final CMethodBeanFactory beanFactory;
    protected final boolean ignoreException;

    protected CAbstractExecutor(CExecutorContext context, CMethodBeanFactory beanFactory, boolean ignoreException) {
        this.context = context;
        this.beanFactory = beanFactory;
        this.ignoreException = ignoreException;
        this.status = Status.NotReady;
    }

    @Override
    public Object getReturnVal() {
        if (status != Status.Stop) {
            throw new CExecutorStatusException("executor not executed successfully.");
        }
        return context.get(CMethodBeanFactory.EXEC_RETURN_VAL_KEY);
    }

    @Override
    public Throwable getThrowable() {
        if (status != Status.Exception) {
            throw new CExecutorStatusException("executor has not encountered an exception.");
        }
        return (Throwable) context.get(CMethodBeanFactory.EXEC_EXCEPTION_KEY);
    }

    @Override
    public boolean isIgnoreException() {
        return ignoreException;
    }

    @Override
    public boolean isExceptionInContext() {
        return context.get(CMethodBeanFactory.EXEC_EXCEPTION_KEY) != null;
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
        throw new CUnsupportedOperationException("save state unsupported.");
    }

    @Override
    public void loadState() {
        throw new CUnsupportedOperationException("load state unsupported.");
    }
}
