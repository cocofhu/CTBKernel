package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CDefaultExecutionRuntime implements CExecutionRuntime{

    public static final String EXEC_RETURN_VAL_KEY = "EXEC_RETURN_VAL_KEY";
    public static final String EXEC_EXCEPTION_KEY = "EXEC_EXCEPTION_KEY";
    public static final String EXEC_CONTEXT_KEY = "EXEC_CONTEXT_KEY";

    // 当前层，每个执行器都将拥有一个唯一的层
    private volatile CDefaultLayerDataSet<String, Object> currentLayer;
    private volatile long lastTime = 0;


    private final ReentrantLock lock;
    private final List<Long> timeElapsed ;
    private final List<CExecutorRuntimeType> types;
    private final List<CExecutor> executors;
    private final List<Integer> layers;





    public CDefaultExecutionRuntime() {

        lastTime = System.currentTimeMillis();
        timeElapsed = new ArrayList<>(16);
        types = new ArrayList<>(16);
        executors = new ArrayList<>(16);
        layers = new ArrayList<>(16);
        lock = new ReentrantLock();
        this.currentLayer = new CDefaultLayerDataSet<>();
        long currentTime = System.currentTimeMillis();
        timeElapsed.add(currentTime - lastTime);
    }

    @Override
    public CDefaultLayerDataSet<String, Object> getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public void startNew(CReadOnlyDataSet<String, Object> attachment, boolean copyCurrent, CExecutorRuntimeType type, CExecutor executor) {
        try {
            lock.lock();
            CDefaultLayerDataSet<String, Object> layer = currentLayer.newLayer();
            if (copyCurrent) {
                currentLayer.entries(0).forEach(e -> layer.put(e.getKey(), e.getValue()));
            }
            if (attachment != null) {
                attachment.entries().forEach(e -> layer.put(e.getKey(), e.getValue()));
            }
            this.currentLayer = layer;
            this.lastTime = System.currentTimeMillis();
            this.types.add(type);




        }finally {
            lock.unlock();
        }
    }

    @Override
    public void stopCurrent() {

    }


    @Override
    public String toString() {
        AsciiTable table = new AsciiTable();
        table.addRule();
        int depth = currentLayer.depth();
        table.addRow(null, null, null, null, "Execution Summary").setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        table.addRow(null, "BuildCost:", timeElapsed.get(0) + "ms", "TimeElapsed:", (Long) timeElapsed.stream().mapToLong(a -> a).sum() + "ms");
        table.addRule();
        table.addRow("Id", "TimeElapsed", "ReturnValue", "Exception", "Context");
        table.addRule();
        for (int i = 0; i < depth; i++) {
            StringBuilder sb = new StringBuilder();
            currentLayer.entries(depth - 1 - i).stream()
                    .filter(e -> e.getValue() != this && !e.getKey().equals(EXEC_RETURN_VAL_KEY) && !e.getKey().equals(EXEC_EXCEPTION_KEY) && !e.getKey().equals(EXEC_CONTEXT_KEY))
                    .map(e -> "" + e.getKey() + "=" + e.getValue() + " <br>").forEach(sb::append);


            table.addRow(i, timeElapsed.get(i + 1) + "ms", "200", "null", sb).setTextAlignment(TextAlignment.LEFT);
            table.getRenderer().setCWC(new CWC_LongestWordMin(3));
            table.addRule();
        }
        return table.render(100);
    }


}
