package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author cocofhu
 */
public class CDefaultExecutionRuntime implements CExecutionRuntime {

    // 当前层，每个执行器都将拥有一个唯一的层
    private volatile CDefaultLayerData<String, Object> currentLayer;
    private volatile long lastTime;


    private final ReentrantLock lock;
    private final List<Long> timeElapsed;
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
        this.currentLayer = new CDefaultLayerData<>();
        long currentTime = System.currentTimeMillis();
        timeElapsed.add(currentTime - lastTime);
    }

    @Override
    public CDefaultLayerData<String, Object> getCurrentLayer() {
        return currentLayer;
    }

    @Override
    public void startNew(CReadOnlyData<String, Object> attachment, boolean copyCurrent,
                         CExecutorRuntimeType type, CExecutor executor) {
        try {
            lock.lock();
            CDefaultLayerData<String, Object> layer = currentLayer.newLayer();
            layer.put(EXEC_CONTEXT_KEY, this);
            if (copyCurrent) {
                currentLayer.entries(0).forEach(e -> layer.put(e.getKey(), e.getValue()));
            }
            if (attachment != null) {
                attachment.entries().forEach(e -> layer.put(e.getKey(), e.getValue()));
            }
            this.currentLayer = layer;
            long currentTime = System.currentTimeMillis();
            this.types.add(type);
            this.timeElapsed.add(currentTime - this.lastTime);
            lastTime = currentTime;
        } finally {
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
        table.addRow(null, null, null, null, null, "Execution Summary").setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        table.addRow(null, "BuiltCost:", timeElapsed.get(0) + "ms", null, "TimeElapsed:", (Long) timeElapsed.stream().mapToLong(a -> a).sum() + "ms");
        table.addRule();
        table.addRow("Id", "Type", "TimeElapsed", "ReturnValue", "Exception", "Context");
        table.addRule();
        for (int i = 0; i < depth; i++) {
            StringBuilder sb = new StringBuilder();
            currentLayer.entries(depth - 1 - i).stream()
                    .filter(e -> e.getValue() != this && !e.getKey().equals(EXEC_RETURN_VAL_KEY) && !e.getKey().equals(EXEC_EXCEPTION_KEY) && !e.getKey().equals(EXEC_CONTEXT_KEY))
                    .map(e -> "" + e.getKey() + "=" + e.getValue() + " <br>").forEach(sb::append);

            Object returnVal = "null";

            table.addRow(i, types.get(i), ( i+2 >= timeElapsed.size()? System.currentTimeMillis() - lastTime : timeElapsed.get(i + 2) ) + "ms", returnVal , "null", sb).setTextAlignment(TextAlignment.LEFT);
            table.getRenderer().setCWC(new CWC_LongestWordMin(3));
            table.addRule();
        }
        return table.render(100);
    }


}
