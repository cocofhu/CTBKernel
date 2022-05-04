package com.cocofhu.ctb.kernel.core.factory.exec;

import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.exception.CExecutorExceptionUnhandledException;
import com.cocofhu.ctb.kernel.exception.CExecutorStatusException;

public class CSimpleExecutor extends CAbstractExecutor {

    private final CExecutorMethod executorMethod;

    public CSimpleExecutor(CExecutorContext context, CMethodBeanFactory beanFactory, CExecutorMethod executorMethod, boolean ignoreException) {
        super(context, beanFactory, ignoreException);
        this.executorMethod = executorMethod;
    }

    public CSimpleExecutor(CExecutorContext context, CMethodBeanFactory beanFactory, CExecutorMethod executorMethod) {
        this(context, beanFactory, executorMethod, false);
    }

    @Override
    public void run() {
        if (status != Status.Ready) {
            throw new CExecutorStatusException("executor is not ready.");
        }
        if (!isIgnoreException() && isExceptionInContext()) {
            // 有未处理的异常
            status = Status.Exception;
            throw new CExecutorExceptionUnhandledException(getThrowable());
        }
        // 清除上一次的异常
        context.put(CMethodBeanFactory.EXEC_EXCEPTION_KEY,null);
        // 设置执行状态, 开始执行
        status = Status.Running;
        beanFactory.invokeMethod(executorMethod, context);
        if (context.get(CMethodBeanFactory.EXEC_EXCEPTION_KEY) != null) {
            status = Status.Exception;
        } else {
            status = Status.Stop;
        }
    }


}
