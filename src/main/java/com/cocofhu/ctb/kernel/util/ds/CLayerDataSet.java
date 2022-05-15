package com.cocofhu.ctb.kernel.util.ds;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>

 * </pre>
 */
public interface CLayerDataSet<K,V> {
    /**
     * 新建层
     */
    CLayerDataSet<K,V> newLayer();

    CLayerDataSet<K,V> getParent();

}
