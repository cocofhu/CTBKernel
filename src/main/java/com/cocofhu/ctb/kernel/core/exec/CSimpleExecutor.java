package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.job.CExecExceptionUnhandledException;
import com.cocofhu.ctb.kernel.exception.job.CExecStatusException;
import com.cocofhu.ctb.kernel.exception.job.CExecBeanMethodInvokeException;
import com.cocofhu.ctb.kernel.exception.job.CExecNoSuchMethodException;
import com.cocofhu.ctb.kernel.util.ReflectionUtils;
import com.cocofhu.ctb.kernel.util.ds.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.util.ds.CDefaultDefaultWritableDataSet;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CSimpleExecutor extends CAbstractExecutor {

    private final ReentrantLock lock = new ReentrantLock();

    private final CExecutorMethod executorMethod;


    public CSimpleExecutor(CExecutionRuntime executorContext, CConfig config, CExecutorMethod executorMethod, boolean ignoreException, CDefaultDefaultReadOnlyDataSet<String,Object> attachment) {
        super(executorContext, config, ignoreException, attachment);
        this.executorMethod = executorMethod;
    }

    public CSimpleExecutor(CExecutionRuntime executorContext, CConfig beanFactoryContext, CExecutorMethod executorMethod) {
        this(executorContext, beanFactoryContext, executorMethod, false, null);
    }



    @Override
    public void run() {
        try {
            lock.lock();
            if (getStatus() != Status.Ready) {
                throw new CExecStatusException(this, "executor is not ready.");
            }
            if (!isIgnoreException() && isExceptionInContext()) {
                // 有未处理的异常
                setStatus(Status.Exception);
                throw new CExecExceptionUnhandledException(getThrowable());
            }
            // 清除上一次的异常

            // 设置执行状态, 开始执行
            setStatus(Status.Running);

            // 获取执行信息 这里可能会抛出 CNoSuchBeanDefinitionException
            CBeanDefinition beanDefinition = config.getBeanFactory().getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
            executorContext.newLayer(attachment,true);
            executorContext.newLayer(null,false);
            Object bean = config.getBeanFactory().getBean(beanDefinition, executorContext.getCurrentLayer());
            Method method = ReflectionUtils.findMethod(bean.getClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes());
            // 检查方法是否存在
            if (method == null) {
                throw new CExecNoSuchMethodException( bean.getClass() + "." + executorMethod.getMethodName(), executorMethod.getParameterTypes());
            }


            CExecutableWrapper executableWrapper = new CExecutableWrapper(method, config, beanDefinition, executorContext.getCurrentLayer());

            try {
                Object returnVal = executableWrapper.execute(bean);
                executorContext.setReturnVal(returnVal);

            } catch (InstantiationException | IllegalAccessException e) {
                throw new CExecBeanMethodInvokeException(executorMethod, e);
            } catch (InvocationTargetException e) {
                executorContext.setException(e.getTargetException());
            }

            if (executorContext.hasExceptionRecently()) {
                setStatus(Status.Exception);
            } else {
                setStatus(Status.Stop);
            }
        } finally {

            lock.unlock();
        }
    }

    @Override
    public String toString() {
        return "CSimpleExecutor{" +
                "lock=" + lock +
                ", executorMethod=" + executorMethod +
                ", executorContext=" + executorContext +
                ", config=" + config +
                ", ignoreException=" + ignoreException +
                ", attachment=" + attachment +
                '}';
    }
}
