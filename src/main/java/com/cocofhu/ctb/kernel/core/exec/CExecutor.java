package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.config.CReadOnlyDataSet;
import com.cocofhu.ctb.kernel.exception.CJobException;

import java.util.Map;

/**
 * @author cocofhu
 */
public interface CExecutor extends Runnable{


    String EXEC_RETURN_VAL_KEY = "EXEC_RETURN_VAL_KEY";
    String EXEC_EXCEPTION_KEY = "EXEC_EXCEPTION_KEY";

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


    boolean isIgnoreException();

    boolean isExceptionInContext();

    boolean isExecutedSuccessfully();

    Throwable getThrowable() throws CJobException;
    Object getReturnVal() throws CJobException;

    void setAttachment(CReadOnlyDataSet<String, Object> attachment);


    @Override
    void run() throws CJobException;
}
