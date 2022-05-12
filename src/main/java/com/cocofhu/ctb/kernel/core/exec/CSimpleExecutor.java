package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.job.CJobExceptionUnhandledException;
import com.cocofhu.ctb.kernel.exception.job.CJobStatusException;
import com.cocofhu.ctb.kernel.exception.job.CJobBeanMethodInvokeException;
import com.cocofhu.ctb.kernel.exception.job.CNoSuchMethodException;
import com.cocofhu.ctb.kernel.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CSimpleExecutor extends CAbstractExecutor {

    private final ReentrantLock lock = new ReentrantLock();

    private final CExecutorMethod executorMethod;


    public CSimpleExecutor(CExecutorContext executorContext, CConfig config, CExecutorMethod executorMethod, boolean ignoreException, CDefaultDefaultReadOnlyDataSet<String,Object> attachment) {
        super(executorContext, config, ignoreException, attachment);
        this.executorMethod = executorMethod;
    }

    public CSimpleExecutor(CExecutorContext executorContext, CConfig beanFactoryContext, CExecutorMethod executorMethod) {
        this(executorContext, beanFactoryContext, executorMethod, false, null);
    }



    @Override
    public void run() {
        try {
            lock.lock();
            if (getStatus() != Status.Ready) {
                throw new CJobStatusException(this, "executor is not ready.");
            }
            if (!isIgnoreException() && isExceptionInContext()) {
                // 有未处理的异常
                setStatus(Status.Exception);
                throw new CJobExceptionUnhandledException(getThrowable());
            }
            // 清除上一次的异常
            executorContext.put(EXEC_EXCEPTION_KEY,null);
            // 设置执行状态, 开始执行
            setStatus(Status.Running);

            // 获取执行信息 这里可能会抛出 CNoSuchBeanDefinitionException
            CBeanDefinition beanDefinition = config.getBeanFactory().getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
            CDefaultLayerDataSet<String, Object> newContext = executorContext.newLayer();
            newContext.putAll(attachment);

            Object bean = config.getBeanFactory().getBean(beanDefinition, newContext);
            Method method = ReflectionUtils.findMethod(bean.getClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes());
            // 检查方法是否存在
            if (method == null) {
                throw new CNoSuchMethodException( bean.getClass() + "." + executorMethod.getMethodName(), executorMethod.getParameterTypes());
            }


            CExecutableWrapper executableWrapper = new CExecutableWrapper(method, config, beanDefinition, newContext);

            try {
                Object returnVal = executableWrapper.execute(bean);
                executorContext.put(EXEC_RETURN_VAL_KEY, returnVal);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CJobBeanMethodInvokeException(executorMethod, e);
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
