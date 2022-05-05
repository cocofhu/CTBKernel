package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.exception.CExecutorExceptionUnhandledException;
import com.cocofhu.ctb.kernel.exception.CExecutorStatusException;
import com.cocofhu.ctb.kernel.exception.CBeanMethodInvokeException;
import com.cocofhu.ctb.kernel.exception.CNoSuchMethodException;
import com.cocofhu.ctb.kernel.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CSimpleExecutor extends CAbstractExecutor {

    private final CExecutorMethod executorMethod;
    private final ReentrantLock lock = new ReentrantLock();

    public CSimpleExecutor(CExecutorContext executorContext, CTBContext beanFactoryContext, CExecutorMethod executorMethod, boolean ignoreException) {
        super(executorContext, beanFactoryContext, ignoreException);
        this.executorMethod = executorMethod;
    }

    public CSimpleExecutor(CExecutorContext executorContext, CTBContext beanFactoryContext, CExecutorMethod executorMethod) {
        this(executorContext, beanFactoryContext, executorMethod, false);
    }



    @Override
    public void run() {
        try {
            lock.lock();
            if (getStatus() != Status.Ready) {
                throw new CExecutorStatusException("executor is not ready.");
            }
            if (!isIgnoreException() && isExceptionInContext()) {
                // 有未处理的异常
                setStatus(Status.Exception);
                throw new CExecutorExceptionUnhandledException(getThrowable());
            }
            // 清除上一次的异常
            executorContext.put(EXEC_EXCEPTION_KEY,null);
            // 设置执行状态, 开始执行
            setStatus(Status.Running);


            CBeanDefinition beanDefinition = beanFactoryContext.getBeanFactory().getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
            Object bean = beanFactoryContext.getBeanFactory().getBean(beanDefinition);

            Method method = ReflectionUtils.findMethod(bean.getClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes());
            if (method == null) {
                throw new CNoSuchMethodException(executorMethod.getBeanName() + "." + executorMethod.getMethodName(), executorMethod.getParameterTypes());
            }
            CTBContext ctx = beanFactoryContext.newCTBContext();
            ctx.put(EXEC_CONTEXT_KEY, executorContext);

            CExecutableWrapper executableWrapper = new CExecutableWrapper(method, ctx, beanDefinition);

            try {
                Object returnVal = executableWrapper.execute(bean);
                executorContext.put(EXEC_RETURN_VAL_KEY, returnVal);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CBeanMethodInvokeException("cannot call method of " + method.getName() + ", exception message : " + e.getMessage());
            } catch (InvocationTargetException e) {
                executorContext.put(EXEC_EXCEPTION_KEY,e.getTargetException());
            }

            if (executorContext.get(EXEC_EXCEPTION_KEY) != null) {
                setStatus(Status.Exception);
            } else {
                setStatus(Status.Stop);
            }
        } finally {
            lock.unlock();
        }
    }


}
