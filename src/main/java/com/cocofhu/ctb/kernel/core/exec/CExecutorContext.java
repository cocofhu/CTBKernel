package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author cocofhu
 */
public class CExecutorContext extends CDefaultDefaultWritableDataSet implements CDefaultLayerDataSet {

    public static class CReadOnlyEntry extends CPair<String,Object> implements CReadOnlyDataSet.CReadOnlyEntry<String,Object> {
        public CReadOnlyEntry(String first, Object second) {
            super(first, second);
        }
        @Override
        public String getKey() {
            return first;
        }
        @Override
        public Object getValue() {
            return second;
        }
    }

    /**
     * 执行作用域的参数
     */
    private final Map<String,Object> executionData;

    public CExecutorContext(Map<String,Object> executionData) {
        this.executionData = new ConcurrentHashMap<>();
        // 这里拷贝一份数据，避免并发时出现问题
        if(executionData != null){
            this.executionData.putAll(executionData);
        }
    }
    public CExecutorContext() {
        this(null);
    }

    @Override
    public Object get(String key){
        return executionData.get(key);
    }

    @Override
    public Set<? extends CReadOnlyDataSet.CReadOnlyEntry<String, Object>> entries() {
        Set<CReadOnlyEntry> entries = new HashSet<>();
        entries.add(new CReadOnlyEntry("1",null));
        return entries;
    }


    @Override
    public Object put(String key,Object val){
        if(key == null) {
            return null;
        }
        if(val == null) {
            return executionData.remove(key);
        }
        return executionData.put(key,val);
    }

    @Override
    public Object remove(String key){
        return this.put(key,null);
    }

    @Override
    public void putAll(CReadOnlyDataSet<? extends String, ?> dataSet) {
        dataSet.entries().forEach(e -> this.put(e.getKey(),e.getValue()));
    }

    @Override
    public CExecutorContext newLayer() {
        return null;
    }

    @Override
    public void backLayer(int layer) {

    }
}
