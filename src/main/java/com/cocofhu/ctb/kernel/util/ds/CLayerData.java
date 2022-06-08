package com.cocofhu.ctb.kernel.util.ds;


import java.util.List;
import java.util.Map;
import java.util.Set;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>
 *
 * </pre>
 */
public interface CLayerData<K, V> extends CWritableData<K, V> {
    /**
     * 新建层
     */
    CLayerData<K, V> newLayer();

    /**
     * 新建层 并复制顶层的所有数据到该层
     */
    CLayerData<K, V> newCopiedTopLayer();

    CLayerData<K, V> newLayer(CReadOnlyData<K, V> data) ;

    List<? extends CLayerData<K, V>> subLayers();


    /**
     * 从集合中获取指定key对应的元素，只搜索第depth层
     * eg. depth=0 将只搜索当前层
     */
    V get(K key, int depth);

    /**
     * 获得指定层上对所有Entry，depth从0开始
     */
    Set<? extends CReadOnlyEntry<K, V>> entries(int depth);

    /**
     * 获得层对深度 默认是0
     */
    int depth();

    /**
     * 获得指定层上的Map
     */
    Map<K, V> toReadOnlyMap(int depth);


    // ------------------------Extend Methods------------------------------------

    /**
     * 将指定的key-val对 加入到集合， 返回原来存在的元素(Only Current Layer)
     */
    V put(K key, V val);

    /**
     * 删除指定的Key对应的元素，返回原来存在的元素(Only Current Layer)
     */
    V remove(K key);

    /**
     * 将指定数据集中的元素全部添加到集合中(Only Current Layer)
     */
    void putAll(CReadOnlyData<? extends K, ? extends V> dataset);

}
