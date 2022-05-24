package com.cocofhu.ctb.kernel.util.ds;


import java.util.Set;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>

 * </pre>
 */
public interface CLayerData<K,V> extends CWritableData<K, V> {
    /**
     * 新建层
     */
    CLayerData<K,V> newLayer();


    /**
     * 从集合中获取指定key对应的元素，只搜索第depth层
     * eg. depth=0 将只搜索当前层
     */
    V get(K key, int depth);
    
    Set<? extends CReadOnlyEntry<K, V>> entries(int depth);

    int depth();


    // ------------------------Extend Methods------------------------------------
    /**
     * 将指定的key-val对 加入到集合， 返回原来存在的元素(Only Current Layer)
     */
    V put(K key,V val);

    /**
     * 删除指定的Key对应的元素，返回原来存在的元素(Only Current Layer)
     */
    V remove(K key);

    /**
     * 将指定数据集中的元素全部添加到集合中(Only Current Layer)
     */
    void putAll(CReadOnlyData<? extends K, ? extends V> dataSet);

}
