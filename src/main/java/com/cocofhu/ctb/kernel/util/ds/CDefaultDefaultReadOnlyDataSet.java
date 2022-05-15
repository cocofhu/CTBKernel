package com.cocofhu.ctb.kernel.util.ds;

import java.util.HashSet;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

public class CDefaultDefaultReadOnlyDataSet<K,V> implements CReadOnlyDataSet<K, V> {

    protected Map<K,V> dataset;

    protected CDefaultDefaultReadOnlyDataSet(Map<K,V> dataImpl, CDefaultDefaultReadOnlyDataSet<K,V> dataset) {
        this.dataset = dataImpl;
        if(this.dataset == null){
            this.dataset = new ConcurrentHashMap<>();
        }
        if(dataset != null) {
            Set<CDefaultReadOnlyEntry<K, V>> entries = dataset.entries();
            if (entries != null) {
                entries.forEach(e->this.dataset.put(e.getKey(), e.getValue()));
            }
        }
    }

    public CDefaultDefaultReadOnlyDataSet(CDefaultDefaultReadOnlyDataSet<K,V> dataset) {
        this(null,dataset);
    }

    public CDefaultDefaultReadOnlyDataSet() {
        this(null,null);
    }



    public static class CDefaultReadOnlyEntry<K,V> implements CReadOnlyDataSet.CReadOnlyEntry<K,V> {
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
            return o instanceof CReadOnlyDataSet.CReadOnlyEntry
                    && Objects.equals(getKey() ,((CReadOnlyEntry<?, ?>)o).getKey());
        }
        @Override
        public int hashCode() {
            return Objects.hash(getKey());
        }
    }

    @Override
    public V get(K key) {
        return dataset.get(key);
    }

    @Override
    public Set<CDefaultDefaultReadOnlyDataSet.CDefaultReadOnlyEntry<K, V>> entries() {
        Set<CDefaultReadOnlyEntry<K,V>> entries = new HashSet<>();
        dataset.forEach((k,v)->entries.add(new CDefaultReadOnlyEntry<>(k,v)));
        return entries;
    }

}
