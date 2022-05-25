package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.exec.*;
import com.cocofhu.ctb.kernel.util.CReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CSimpleExecutor extends CAbstractExecutor {

    private final ReentrantLock lock = new ReentrantLock();

    private final CExecutorMethod executorMethod;

    /**
     * @param executionRuntime   执行器的上下文，用于存放执行过程中的参数
     * @param executorDefinition 任务定义
     * @param config             BeanFactory的上下文，用于获得框架的支持
     */
    public CSimpleExecutor(CDefaultExecutionRuntime executionRuntime, CExecutorDefinition executorDefinition, CConfig config) {
        super(executionRuntime, executorDefinition, config);
        if(executorDefinition == null){
            throw new CExecBadDefinitionException("executor definition must be not null. ");
        }
        this.executorMethod = executorDefinition.getMethod();
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

            // 设置执行状态, 开始执行
            setStatus(Status.Running);

            // 获取执行信息 这里可能会抛出 CNoSuchBeanDefinitionException
            CBeanDefinition beanDefinition = config.getBeanFactory().getBeanDefinition(executorMethod.getBeanName(), executorMethod.getBeanClass());
            Object bean = config.getBeanFactory().getBean(beanDefinition, executionRuntime.getCurrentLayer());
            Method method = CReflectionUtils.findMethod(bean.getClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes());
            // 检查方法是否存在
            if (method == null) {
                throw new CExecNoSuchMethodException( bean.getClass() + "." + executorMethod.getMethodName(), executorMethod.getParameterTypes());
            }
            // 复制参数
            executionRuntime.startNew(getExecutorDefinition().getAttachment(),true, CExecutionRuntime.CExecutorRuntimeType.ARGS_COPY, this);
            // 启动当前任务环境
            executionRuntime.startNew(null,false, CExecutionRuntime.CExecutorRuntimeType.SIMPLE, this);
            CExecutableWrapper executableWrapper = new CExecutableWrapper(method, config, beanDefinition, executionRuntime.getCurrentLayer());

            try {
                Object returnVal = executableWrapper.execute(bean);
                executionRuntime.setReturnVal(returnVal);
            } catch (InstantiationException | IllegalAccessException e) {
                throw new CExecBeanMethodInvokeException(executorMethod, e);
            } catch (InvocationTargetException e) {
                executionRuntime.setException(e.getTargetException());
            }

            if (executionRuntime.hasException()) {
                setStatus(Status.Exception);
            } else {
                setStatus(Status.Stop);
            }
        } finally {
            lock.unlock();
        }
    }

}
