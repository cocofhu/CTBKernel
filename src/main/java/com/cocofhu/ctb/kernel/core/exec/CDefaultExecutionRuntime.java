package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.Stack;
import java.util.UUID;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CDefaultExecutionRuntime implements CExecutionRuntime {

    // 当前层，每个执行器都将拥有一个唯一的层
    private volatile CDefaultLayerData<String, Object> currentLayer = new CDefaultLayerData<>();
    private final ReentrantLock lock = new ReentrantLock();

    private Stack<Execution> executionStack = new Stack<>();


    private static class Execution{
        public String uuid;
        public long start;
        public long end = -1;
        public CExecutor executor;
        public CExecutorRuntimeType type;
        public List<Execution> executions;
        public CDefaultLayerData<String, Object> layer;

        public Execution(String uuid, CExecutor executor, CExecutorRuntimeType type, CDefaultLayerData<String, Object> layer) {
            this.uuid = uuid;
            this.executor = executor;
            this.type = type;
            this.layer = layer;
            this.executions = new ArrayList<>();
            this.start = System.currentTimeMillis();
        }
        public void finish(){
            end = System.currentTimeMillis();
        }
    }


    public CDefaultExecutionRuntime() {

    }

    @Override
    public CDefaultLayerData<String, Object> getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public UUID start(CReadOnlyData<String, Object> attachment, CExecutorRuntimeType type, CExecutor executor) {
        String uuid = UUID.randomUUID().toString();
        try {
            lock.lock();
            CDefaultLayerData<String, Object> layer = currentLayer.newLayer();
            Execution execution = new Execution(uuid, executor, type, layer);

            switch (type){
                case ARGS_COPY:
                    currentLayer.toReadOnlyMap(0).forEach(layer::put);
                    layer.putAll(attachment);
                    execution.finish();
                    break;
                case SIMPLE:
                    layer.putAll(attachment);

                case SERVICE:
                case LIST:
                    executionStack.push(execution);
                case RESTORE:
            }
            if(!executionStack.empty()){
                executionStack.peek().executions.add(execution);
            }
            currentLayer = layer;
        } finally {
            lock.unlock();
        }
        return null;
    }

    @Override
    public void finish(UUID uuid) {

    }


    @Override
    public String toString() {
//        AsciiTable table = new AsciiTable();
//        table.addRule();
//        int depth = currentLayer.depth();
//        table.addRow(null, null, null, null, null, "Execution Summary").setTextAlignment(TextAlignment.CENTER);
//        table.addRule();
//        table.addRow(null, "BuiltCost:", timeElapsed.get(0) + "ms", null, "TimeElapsed:", (Long) timeElapsed.stream().mapToLong(a -> a).sum() + "ms");
//        table.addRule();
//        table.addRow("Id", "Type", "TimeElapsed", "ReturnValue", "Exception", "Context");
//        table.addRule();
//        for (int i = 0; i < depth; i++) {
//            StringBuilder sb = new StringBuilder();
//            currentLayer.entries(depth - 1 - i).stream()
//                    .filter(e -> e.getValue() != this && !e.getKey().equals(EXEC_RETURN_VAL_KEY) && !e.getKey().equals(EXEC_EXCEPTION_KEY) && !e.getKey().equals(EXEC_CONTEXT_KEY))
//                    .map(e -> "" + e.getKey() + "=" + e.getValue() + " <br>").forEach(sb::append);
//
//            Object returnVal = "null";
//
//            table.addRow(i, types.get(i), (i + 2 >= timeElapsed.size() ? System.currentTimeMillis() - lastTime : timeElapsed.get(i + 2)) + "ms", returnVal, "null", sb).setTextAlignment(TextAlignment.LEFT);
//            table.getRenderer().setCWC(new CWC_LongestWordMin(3));
//            table.addRule();
//        }
//        return table.render(100);
        return null;
    }


}
