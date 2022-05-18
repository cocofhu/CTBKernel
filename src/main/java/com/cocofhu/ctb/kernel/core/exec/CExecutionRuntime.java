package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;
import de.vandermeer.asciitable.AsciiTable;
import de.vandermeer.asciitable.CWC_LongestWordMax;
import de.vandermeer.asciitable.CWC_LongestWordMin;
import de.vandermeer.skb.interfaces.document.TableRowStyle;
import de.vandermeer.skb.interfaces.transformers.textformat.TextAlignment;

import java.util.Arrays;
import java.util.Set;

/**
 *
 * @author cocofhu
 */
public class CExecutionRuntime {

    public static final String EXEC_RETURN_VAL_KEY = "EXEC_RETURN_VAL_KEY";
    public static final String EXEC_EXCEPTION_KEY = "EXEC_EXCEPTION_KEY";
    public static final String EXEC_CONTEXT_KEY = "EXEC_CONTEXT_KEY";

    // 当前层，每个执行器都将拥有一个唯一的层
    private CDefaultLayerDataSet<String, Object> currentLayer;

    public CExecutionRuntime() {
        currentLayer = new CDefaultLayerDataSet<>();
        currentLayer.put(EXEC_CONTEXT_KEY, this);
    }

    public CDefaultLayerDataSet<String, Object> getCurrentLayer() {
        return currentLayer;
    }

    public void newLayer(CReadOnlyDataSet<String,Object> attachment) {
        CDefaultLayerDataSet<String, Object> layer = currentLayer.newLayer();
        currentLayer.entries(0).forEach(e-> layer.put(e.getKey(),e.getValue()));
        attachment.entries().forEach(e-> layer.put(e.getKey(),e.getValue()));
        this.currentLayer = layer;
    }


    /**
     * 当前层下是否发生过异常
     */
    public boolean hasExceptionRecently(){
        return getExceptionRecently() != null ;
    }

    /**
     * 获取当前层下的异常
     */
    public Object getExceptionRecently(){
        return currentLayer.get(EXEC_EXCEPTION_KEY, 0);
    }

    /**
     * 获得当前层下的返回值
     */
    public Object getReturnValRecently(){
        return currentLayer.get(EXEC_RETURN_VAL_KEY);
    }

    /**
     * 在当前层上获得返回值
     */
    public void setReturnVal(Object returnVal){
        currentLayer.put(EXEC_RETURN_VAL_KEY, returnVal);
    }
    /**
     * 在当前层上获得异常
     */
    public void setException(Throwable throwable){
        currentLayer.put(EXEC_EXCEPTION_KEY, throwable);
    }


    @Override
    public String toString() {
        AsciiTable table = new AsciiTable();
        table.addRule();
        int depth = currentLayer.depth();
        table.addRow(null, null,null,null, "Execution Summary").setTextAlignment(TextAlignment.CENTER);
        table.addRule();
        table.addRow(null, "ExecutorCount:", depth, "TimeElapsed:", 1230 + "ms");
        table.addRule();
        table.addRow("Id","TimeElapsed","ReturnValue", "Exception" ,"Context");
        table.addRule();
        for (int i = 0; i < depth; i++) {
            StringBuilder sb = new StringBuilder();
            currentLayer.entries(depth - 1-i).stream()
                    .filter(e-> e.getValue() != this && !e.getKey().equals(EXEC_RETURN_VAL_KEY) && !e.getKey().equals(EXEC_EXCEPTION_KEY) && !e.getKey().equals(EXEC_CONTEXT_KEY))
                    .map(e -> "" + e.getKey() + "=" + e.getValue() + " <br>").forEach(sb::append);


            table.addRow(i,"123ms","200","null", sb).setTextAlignment(TextAlignment.LEFT);
            table.getRenderer().setCWC(new CWC_LongestWordMin(3));
            table.addRule();
        }
        return table.render(100);
    }
}
