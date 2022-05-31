package com.cocofhu.ctb.kernel.util.ds;

import java.util.*;
import java.util.concurrent.ConcurrentHashMap;

public class CDefaultReadOnlyData<K,V> implements CReadOnlyData<K, V> {

    protected Map<K,V> dataset;

    /**
     * 创建一个只读的数据集
     * @param dataImpl  数据的实现 默认使用ConcurrentHashMap
     * @param dataset   根据已有数据创建，浅拷贝
     */
    protected CDefaultReadOnlyData(Map<K,V> dataImpl, CReadOnlyData<K,V> dataset) {
        this.dataset = dataImpl;
        if(this.dataset == null){
            this.dataset = new ConcurrentHashMap<>();
        }
        if(dataset != null) {
            Set<? extends CReadOnlyEntry<K, V>> entries = dataset.entries();
            if (entries != null) {
                entries.forEach(e->this.dataset.put(e.getKey(), e.getValue()));
            }
        }
    }


    public static class CDefaultReadOnlyEntry<K,V> implements CReadOnlyData.CReadOnlyEntry<K,V> {
        private final K key;
        private final V val;
        public CDefaultReadOnlyEntry(K key, V val) {
            this.key = key;
            this.val =val;
        }
        @Override
        public K getKey() {
            return key;
        }
        @Override
        public V getValue() {
            return val;
        }
        @Override
        public boolean equals(Object o) {
            return o instanceof CReadOnlyData.CReadOnlyEntry
                    && Objects.equals(getKey() ,((CReadOnlyEntry<?, ?>)o).getKey());
        }
        @Override
        public int hashCode() {
            return Objects.hash(getKey());
        }

        @Override
        public String toString() {
            return key + "=" + val;
        }
    }

    @Override
    public V get(K key) {
        return dataset.get(key);
    }


    @Override
    public Set<CDefaultReadOnlyData.CDefaultReadOnlyEntry<K, V>> entries() {
        Set<CDefaultReadOnlyEntry<K,V>> entries = new HashSet<>();
        dataset.forEach((k,v)->entries.add(new CDefaultReadOnlyEntry<>(k,v)));
        return entries;
    }

    @Override
    public String toString() {
        Map<K, V> map = toReadOnlyMap();
        return map!= null ? map.toString() : "null";
    }

    @Override
    public Map<K, V> toReadOnlyMap() {
        return Collections.unmodifiableMap(dataset);
    }
}
