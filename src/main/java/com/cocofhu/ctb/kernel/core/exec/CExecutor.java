package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * @author cocofhu
 */
public interface CExecutor{

    enum Status{
        // 未就绪，任务的执行可能需要必要条件
        NotReady,
        // 已就绪，任务随时可以被调度执行
        Ready,
        // 运行中，任务正在执行中
        Running,
        // 执行完毕，任务正常终止
        Stop,
        // 任务异常终止
        Exception
    }

    /**
     *  获取当前的执行状态
     */
    Status getStatus();

    void setStatus(Status status);

    /**
     * 将当前的任务状态保存
     */
    void saveState();

    /**
     * 读取保存的任务状态
     */
    void loadState();


    default boolean isIgnoreException(){
        CExecutorDefinition definition = getExecutorDefinition();
        return definition != null && definition.isIgnoreException();
    }


    default boolean isExecutedSuccessfully(){
        return getStatus() == Status.Stop;
    }

    // 数据与行为解耦合，不要在这里指定任何参数
    void run(CExecutionRuntime runtime) throws CExecException;


    CExecutorDefinition getExecutorDefinition();



}
