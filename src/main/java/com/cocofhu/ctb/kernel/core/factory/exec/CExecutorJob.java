package com.cocofhu.ctb.kernel.core.factory.exec;

import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;

public class CExecutorJob extends CAbstractExecutor {
    
    
    private final CExecutor[] executors;
    private int which = 0;

    protected CExecutorJob(CExecutorContext context, CMethodBeanFactory beanFactory, boolean ignoreException, CExecutor[] executors) {
        super(context, beanFactory, ignoreException);
        this.executors = executors;
    }

    @Override
    public void run() {
        Throwable lastThrowable = null;
        for (int i = 0; i < executors.length; i++) {
            which = i;
            CExecutor executor = executors[i];
            executor.run();
        }
    }
}
