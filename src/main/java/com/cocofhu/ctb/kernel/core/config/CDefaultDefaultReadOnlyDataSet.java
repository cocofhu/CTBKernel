package com.cocofhu.ctb.kernel.core.config;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CDefaultDefaultReadOnlyDataSet implements CReadOnlyDataSet<String, Object> {

    protected Map<String,Object> dataset;

    protected CDefaultDefaultReadOnlyDataSet(Map<String,Object> dataImpl, CDefaultDefaultReadOnlyDataSet dataset) {
        this.dataset = dataImpl;
        if(this.dataset == null){
            this.dataset = new ConcurrentHashMap<>();
        }
        if(dataset != null) {
            Set<? extends CReadOnlyDataSet.CReadOnlyEntry<String, Object>> entries = dataset.entries();
            if (entries != null) {
                entries.forEach(e->this.dataset.put(e.getKey(), e.getValue()));
            }
        }
    }

    public CDefaultDefaultReadOnlyDataSet(CDefaultDefaultReadOnlyDataSet dataset) {
        this(null,dataset);
    }

    public CDefaultDefaultReadOnlyDataSet() {
        this(null,null);
    }



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

    @Override
    public Object get(String key) {
        return dataset.get(key);
    }

    @Override
    public Set<? extends CReadOnlyDataSet.CReadOnlyEntry<String, Object>> entries() {
        Set<CDefaultDefaultReadOnlyDataSet.CReadOnlyEntry> entries = new HashSet<>();
        dataset.forEach((k,v)->entries.add(new CDefaultDefaultReadOnlyDataSet.CReadOnlyEntry(k,v)));
        return entries;
    }

}
