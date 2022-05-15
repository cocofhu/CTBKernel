package com.cocofhu.ctb.kernel.util.ds;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public interface CReadOnlyDataSet<K,V> extends Serializable {
    V get(K key);

    Set<? extends CReadOnlyEntry<K,V>> entries();


    interface CReadOnlyEntry<K,V> {
        K getKey();
        V getValue();
    }

    default Map<K,V> toMap(){
        Map<K,V> map = new HashMap<>();
        entries().forEach(e -> {
            V value = e.getValue();
            K key = e.getKey();
            if(value != null && key != null){
                map.put(key,value);
            }
        });
        return map;
    }

}
