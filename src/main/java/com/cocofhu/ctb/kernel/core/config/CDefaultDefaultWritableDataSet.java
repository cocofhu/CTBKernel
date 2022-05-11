package com.cocofhu.ctb.kernel.core.config;


public class CDefaultDefaultWritableDataSet extends CDefaultDefaultReadOnlyDataSet implements CWritableDataSet<String, Object> {

    public CDefaultDefaultWritableDataSet(CDefaultDefaultReadOnlyDataSet dataset) {
        super(dataset);
    }

    public CDefaultDefaultWritableDataSet() {
        this(null);
    }

    @Override
    public Object put(String key, Object val) {
        if(key == null) {
            return null;
        }
        if(val == null) {
            return dataset.remove(key);
        }
        return dataset.put(key,val);
    }

    @Override
    public Object remove(String key) {
        return dataset.remove(key);
    }

    @Override
    public void putAll(CReadOnlyDataSet<? extends String, ?> dataSet) {
        if(dataSet != null){
            dataSet.entries().forEach(e -> this.put(e.getKey(),e.getValue()));
        }
    }
}