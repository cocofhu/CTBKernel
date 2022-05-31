package com.cocofhu.ctb.kernel.util.ds;


import java.util.Map;

public class CDefaultWritableData<K, V> extends CDefaultReadOnlyData<K, V> implements CWritableData<K, V> {


    /**
     * 创建一个只读的数据集
     *
     * @param dataImpl 数据的实现 默认使用ConcurrentHashMap
     * @param dataset  根据已有数据创建，浅拷贝
     */
    protected CDefaultWritableData(Map<K, V> dataImpl, CReadOnlyData<K, V> dataset) {
        super(dataImpl, dataset);
    }

    public CDefaultWritableData() {
        super(null, null);
    }

    public CDefaultWritableData(CReadOnlyData<K, V> dataset) {
        super(null, dataset);
    }


    @Override
    public V put(K key, V val) {
        if (key == null) {
            return null;
        }
        if (val == null) {
            return dataset.remove(key);
        }
        return dataset.put(key, val);
    }

    @Override
    public V remove(K key) {
        return dataset.remove(key);
    }

    @Override
    public void putAll(CReadOnlyData<? extends K, ? extends V> dataset) {
        if (dataset != null) {
            dataset.entries().forEach(e -> this.put(e.getKey(), e.getValue()));
        }
    }

    @Override
    public void putAll(Map<? extends K, ? extends V> dataset) {
        if (dataset != null) {
            dataset.forEach(this::put);
        }
    }
}
