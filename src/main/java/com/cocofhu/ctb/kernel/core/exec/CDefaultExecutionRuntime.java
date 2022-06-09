package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.*;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.*;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CDefaultExecutionRuntime implements CExecutionRuntime {


    private final ReentrantLock lock = new ReentrantLock();
    /**
     * 唯一标识
     */
    public final String uuid;
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

    private final CWritableData<String, Object> fullObjects;
    private final CWritableData<String, Object> sinkObjects;

    private volatile boolean isFinished = false;



    private CDefaultExecutionRuntime(CReadOnlyData<String, Object> attachment, CExecutor executor, CExecutorRuntimeType type, CDefaultExecutionRuntime parent) {

        this.executor = executor;
        this.type = type;
        this.executions = new ArrayList<>();
        this.uuid = UUID.randomUUID().toString();
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

    public static CDefaultExecutionRuntime newDefault(CReadOnlyData<String, Object> attachment, CExecutor executor, CExecutorRuntimeType type, CDefaultExecutionRuntime parent){
        return new CDefaultExecutionRuntime(attachment,executor,type,parent);
    }
    public static CDefaultExecutionRuntime newDefault(CReadOnlyData<String, Object> attachment, CExecutor executor, CExecutorRuntimeType type){
        return newDefault(attachment,executor,type,null);
    }
    public static CDefaultExecutionRuntime newDefault(){
        return newDefault(null,null,null,null);
    }

    public void finish() {
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
        lock.unlock();
    }

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


    @Override
    public CExecutionRuntime start(CReadOnlyData<String, Object> attachment, CExecutorRuntimeType type, CExecutor executor) {

        try {
            lock.lock();
            return CDefaultExecutionRuntime.newDefault(attachment, executor, type, this);
        } finally {
            lock.unlock();
        }
    }

    private List<String[]> generateTableData(int recursive,CDefaultExecutionRuntime runtime, int start){

        List<String[]> tables = new ArrayList<>();
        if(recursive >= 0){
            int size = runtime.executions.size();
            if(size == 0){
                tables.add(new String[]{
                        String.valueOf(start),String.valueOf(runtime.uuid), String.valueOf(runtime.type),
                        String.valueOf(runtime.executor.getExecutorDefinition().getName()), String.valueOf(runtime.executor.getExecutorDefinition().getGroup()), String.valueOf(runtime.executor.getExecutorDefinition().getInfo()),
                        String.valueOf(runtime.end-runtime.start), String.valueOf(runtime.getReturnVal()), String.valueOf(runtime.getException()), "sb"});
            }else{
                for (int j = 0; j < size; j++) {
                    tables.addAll(generateTableData(recursive-1,runtime.executions.get(j),start + tables.size()));
                }
            }
        }
        return tables;
    }

    @Override
    public String toString() {
        AsciiTable table = new AsciiTable();
        table.addRule();
        table.addRow(null,null, null, null, null, null, null, null, null, "Execution Summary").setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        table.addRow("Id","UUID", "Type", "Name","Group","Info","TimeCost", "ReturnValue", "Exception", "Context");
        table.addRule();
        generateTableData(2,this,1).forEach((String[] s)->{
            table.addRow((Object []) s);
            table.getRenderer().setCWC(new CWC_LongestWordMin(3));
            table.addRule();
        });


        return table.render(100);
    }

}
