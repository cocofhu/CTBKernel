package com.cocofhu.ctb.kernel.core.config;

import java.util.Map;

public interface CWritableDataSet<K,V> extends CReadOnlyDataSet<K,V>{
    V put(K key,V val);
    V remove(K key);
    void putAll(CReadOnlyDataSet<? extends K, ? extends V> dataSet);
}
