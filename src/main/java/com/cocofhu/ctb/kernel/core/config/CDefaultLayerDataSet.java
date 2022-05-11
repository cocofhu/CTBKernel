package com.cocofhu.ctb.kernel.core.config;

import java.util.HashMap;
import java.util.Set;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>
 *
 * </pre>
 */
public class CDefaultLayerDataSet<K, V> extends CDefaultDefaultWritableDataSet<K, V> implements CLayerDataSet<K, V> {

    private final CDefaultLayerDataSet<K, V> parent;

    private CDefaultLayerDataSet(CDefaultLayerDataSet<K, V> parent) {
        // 除了最顶层，其他的可以使用HashMap提升效率
        // 这也就意味着，除了最顶层都不是线程安全的数据实现
        super(new HashMap<>(), null);
        this.parent = parent;
    }

    public CDefaultLayerDataSet() {
        this(null);
    }

    @Override
    public CDefaultLayerDataSet<K, V> newLayer() {
        return new CDefaultLayerDataSet<>(this);
    }

    @Override
    public CDefaultLayerDataSet<K, V> getParent() {
        return this.parent;
    }

    @Override
    public V get(K key) {
        V o = super.get(key);
        CDefaultLayerDataSet<K, V> p = this.getParent();
        return o == null && p != null ? p.get(key) : o;
    }

    @Override
    public Set<CDefaultReadOnlyEntry<K, V>> entries() {
        CDefaultLayerDataSet<K, V> p = getParent();
        if(p != null) {
            Set<CDefaultReadOnlyEntry<K, V>> entries = p.entries();
            entries.addAll(super.entries());
            return entries;
        }
        return super.entries();
    }
}
