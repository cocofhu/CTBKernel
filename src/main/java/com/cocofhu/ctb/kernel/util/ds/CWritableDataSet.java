package com.cocofhu.ctb.kernel.util.ds;

public interface CWritableDataSet<K,V> extends CReadOnlyDataSet<K,V>{
    /**
     * 将指定的key-val对 加入到集合， 返回原来存在的元素
     */
    V put(K key,V val);

    /**
     * 删除指定的Key对应的元素，返回原来存在的元素
     */
    V remove(K key);

    /**
     * 将指定数据集中的元素全部添加到集合中
     */
    void putAll(CReadOnlyDataSet<? extends K, ? extends V> dataSet);
}
