package com.cocofhu.ctb.kernel.core.config;


import java.util.Map;

public class CDefaultDefaultWritableDataSet<K,V> extends CDefaultDefaultReadOnlyDataSet<K,V> implements CWritableDataSet<K,V> {

    public CDefaultDefaultWritableDataSet(CDefaultDefaultReadOnlyDataSet<K,V> dataset) {
        super(dataset);
    }

    public CDefaultDefaultWritableDataSet() {
        this(null);
    }

    protected CDefaultDefaultWritableDataSet(Map<K,V> dataImpl, CDefaultDefaultWritableDataSet<K,V> dataset){
        super(dataImpl,dataset);
    }

    @Override
    public V put(K key, V val) {
        if(key == null) {
            return null;
        }
        if(val == null) {
            return dataset.remove(key);
        }
        return dataset.put(key,val);
    }

    @Override
    public V remove(K key) {
        return dataset.remove(key);
    }

    @Override
    public void putAll(CReadOnlyDataSet<? extends K, ? extends V> dataSet) {
        if(dataSet != null){
            dataSet.entries().forEach(e -> this.put(e.getKey(),e.getValue()));
        }
    }
}
