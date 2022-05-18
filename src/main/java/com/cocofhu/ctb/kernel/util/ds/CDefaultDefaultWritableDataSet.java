package com.cocofhu.ctb.kernel.util.ds;


import java.util.Map;

public class CDefaultDefaultWritableDataSet<K,V> extends CDefaultDefaultReadOnlyDataSet<K,V> implements CWritableDataSet<K,V> {


    /**
     * 创建一个只读的数据集
     * @param dataImpl  数据的实现 默认使用ConcurrentHashMap
     * @param dataset   根据已有数据创建，浅拷贝
     */
    protected CDefaultDefaultWritableDataSet(Map<K,V> dataImpl, CDefaultDefaultWritableDataSet<K,V> dataset){
        super(dataImpl,dataset);
    }

    public CDefaultDefaultWritableDataSet() {
        super(null, null);
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
