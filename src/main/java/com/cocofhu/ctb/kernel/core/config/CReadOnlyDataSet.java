package com.cocofhu.ctb.kernel.core.config;

import java.util.Set;

public interface CReadOnlyDataSet<K,V> {
    V get(String K);

    Set<? extends CReadOnlyEntry<K,V>> entries();


    interface CReadOnlyEntry<K,V> {
        K getKey();
        V getValue();
    }
}
