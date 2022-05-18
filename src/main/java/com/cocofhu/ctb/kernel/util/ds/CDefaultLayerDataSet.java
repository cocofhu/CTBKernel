package com.cocofhu.ctb.kernel.util.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.BiFunction;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>
 *
 * </pre>
 */
public class CDefaultLayerDataSet<K, V> extends CDefaultDefaultWritableDataSet<K, V> implements CLayerDataSet<K, V> {

    private final CDefaultLayerDataSet<K, V> parent;

    protected CDefaultLayerDataSet(CDefaultLayerDataSet<K, V> parent) {
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
    public V get(K key, int maxRecursive) {
        if(maxRecursive < 0) {
            return null;
        }
        V o = super.get(key);
        CDefaultLayerDataSet<K, V> p = this.parent;
        return o == null && p != null ? p.get(key, maxRecursive - 1) : o;
    }

    @Override
    public Set<CDefaultReadOnlyEntry<K, V>> entries(int depth) {
        if(depth < 0) {
            return null;
        }
        CDefaultLayerDataSet<K, V> cur = this;

        while (depth > 0 && cur != null){
            cur = cur.parent;
            --depth;
        }
        if(cur == null){
            return null;
        }
        Set<CDefaultReadOnlyEntry<K,V>> entries = new HashSet<>();
        cur.dataset.forEach((k,v)->entries.add(new CDefaultReadOnlyEntry<>(k,v)));
        return entries;
    }


    @Override
    public int depth() {
        return parent == null ? 0 : parent.depth() + 1;
    }


    @Override
    public V get(K key) {
        V o = super.get(key);
        CDefaultLayerDataSet<K, V> p = this.parent;
        return o == null && p != null ? p.get(key) : o;
    }

    @Override
    public Set<CDefaultReadOnlyEntry<K, V>> entries() {
        CDefaultLayerDataSet<K, V> p = parent;
        if(p != null) {
            Set<CDefaultReadOnlyEntry<K, V>> entries = p.entries();
            entries.addAll(super.entries());
            return entries;
        }
        return super.entries();
    }
}
