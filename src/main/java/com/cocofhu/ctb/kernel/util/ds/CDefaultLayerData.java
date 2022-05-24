package com.cocofhu.ctb.kernel.util.ds;

import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;

/**
 * 带有层级结构的数据集，实现该接口的类 需要满足新建和回退不同的层
 * 比如:
 * <pre>
 *
 * </pre>
 */
public class CDefaultLayerData<K, V> extends CDefaultWritableData<K, V> implements CLayerData<K, V> {

    private final CDefaultLayerData<K, V> parent;

    protected CDefaultLayerData(CDefaultLayerData<K, V> parent) {
        // 除了最顶层，其他的可以使用HashMap提升效率
        // 这也就意味着，除了最顶层都不是线程安全的数据实现
        super(new HashMap<>(), null);
        this.parent = parent;
    }

    public CDefaultLayerData() {
        this(null);
    }

    @Override
    public CDefaultLayerData<K, V> newLayer() {
        return new CDefaultLayerData<>(this);
    }


    @Override
    public V get(K key, int depth) {
        if (depth < 0) {
            return null;
        } else if (depth == 0) {
            return dataset.get(key);
        } else {
            CDefaultLayerData<K, V> p = this.parent;
            return p != null ? p.get(key, depth - 1) : null;
        }
    }

    @Override
    public Set<CDefaultReadOnlyEntry<K, V>> entries(int depth) {
        if (depth < 0) {
            return null;
        }
        CDefaultLayerData<K, V> cur = this;

        while (depth > 0 && cur != null) {
            cur = cur.parent;
            --depth;
        }
        if (cur == null) {
            return null;
        }
        Set<CDefaultReadOnlyEntry<K, V>> entries = new HashSet<>();
        cur.dataset.forEach((k, v) -> entries.add(new CDefaultReadOnlyEntry<>(k, v)));
        return entries;
    }


    @Override
    public int depth() {
        return parent == null ? 0 : parent.depth() + 1;
    }


    @Override
    public V get(K key) {
        V o = super.get(key);
        CDefaultLayerData<K, V> p = this.parent;
        return o == null && p != null ? p.get(key) : o;
    }

    @Override
    public Set<CDefaultReadOnlyEntry<K, V>> entries() {
        CDefaultLayerData<K, V> p = parent;
        if (p != null) {
            Set<CDefaultReadOnlyEntry<K, V>> entries = p.entries();
            entries.addAll(super.entries());
            return entries;
        }
        return super.entries();
    }
}
