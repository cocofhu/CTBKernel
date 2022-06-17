package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.*;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CDefaultExecutionRuntime implements CExecutionRuntime {


    private final ReentrantLock lock = new ReentrantLock();
    /**
     * 开始时间
     */
    public final long start;
    /**
     * 结束时间
     */
    public long end = 0;
    /**
     * 执行器对象
     */
    public final CExecutor executor;
    /**
     * 类型
     */
    public CExecutorRuntimeType type;
    /**
     * 子Executor的上下文
     */
    public List<CDefaultExecutionRuntime> executions;
    /**
     * 父对象
     */
    private final CDefaultExecutionRuntime parent;

    // 全部的参数
    private final CWritableData<String, Object> fullObjects;
    // 非attachment的参数
    private final CWritableData<String, Object> sinkObjects;

    private volatile boolean isFinished = false;



    private CDefaultExecutionRuntime(CReadOnlyData<String, Object> attachment, CExecutor executor, CExecutorRuntimeType type, CDefaultExecutionRuntime parent) {

        this.executor = executor;
        this.type = type;
        this.executions = new ArrayList<>();
        this.start = System.currentTimeMillis();

        if (parent != null) {
            this.parent = parent;
            parent.executions.add(this);
        } else {
            this.parent = null;
        }
        fullObjects = new CDefaultWritableData<>();
        sinkObjects = new CDefaultWritableData<>();

        Stack<CDefaultExecutionRuntime> parents = new Stack<>();
        CDefaultExecutionRuntime p = this.parent;
        while (p != null) {
            parents.push(p);
            p = p.parent;
        }
        while (!parents.empty()) {
            CDefaultExecutionRuntime pop = parents.pop();
            fullObjects.putAll(pop.fullObjects);
            sinkObjects.putAll(pop.sinkObjects);
        }
        fullObjects.putAll(attachment);

    }

    private static CDefaultExecutionRuntime newDefault(CReadOnlyData<String, Object> attachment, CExecutor executor, CExecutorRuntimeType type, CDefaultExecutionRuntime parent){
        return new CDefaultExecutionRuntime(attachment,executor,type,parent);
    }
    public static CDefaultExecutionRuntime newRoot(){
        return newDefault(null,null,null,null);
    }

    @Override
    public CExecutionRuntime start(CReadOnlyData<String, Object> attachment, CExecutorRuntimeType type, CExecutor executor) {
        try {
            lock.lock();
            return CDefaultExecutionRuntime.newDefault(attachment, executor, type, this);
        } finally {
            lock.unlock();
        }
    }

    @Override
    public boolean finish() {
        try {
            lock.lock();
            if (!isFinished) {
                isFinished = true;
                for (CDefaultExecutionRuntime execution : executions) {
                    if (!execution.isFinished) {
                        isFinished = false;
                        break;
                    }
                }
                if (isFinished) {
                    for (CDefaultExecutionRuntime execution : executions) {
                        sinkObjects.putAll(execution.sinkObjects);
                    }
                    end = System.currentTimeMillis();
                }
            }
        }finally {
            lock.unlock();
        }
        return isFinished;
    }


    // --------------- DATA Methods ---------------
    // --------------- DATA Methods ---------------
    @Override
    public Object get(String key) {
        Object o = fullObjects.get(key);
        return o == null && parent != null ? parent.get(key) : o;
    }

    @Override
    public Set<? extends CReadOnlyData.CReadOnlyEntry<String, Object>> entries() {
        return fullObjects.entries();
    }

    @Override
    public Map<String, Object> toReadOnlyMap() {
        return fullObjects.toReadOnlyMap();
    }

    @Override
    public Object put(String key, Object val) {
        sinkObjects.put(key, val);
        return fullObjects.put(key, val);
    }

    @Override
    public Object remove(String key) {
        sinkObjects.remove(key);
        return fullObjects.remove(key);
    }

    @Override
    public void putAll(CReadOnlyData<? extends String, ?> dataset) {
        sinkObjects.putAll(dataset);
        fullObjects.putAll(dataset);
    }

    @Override
    public void putAll(Map<? extends String, ?> dataset) {
        sinkObjects.putAll(dataset);
        fullObjects.putAll(dataset);
    }

}
