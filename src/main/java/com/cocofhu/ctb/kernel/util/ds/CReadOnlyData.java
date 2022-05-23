package com.cocofhu.ctb.kernel.util.ds;

import java.io.Serializable;
import java.util.HashMap;
import java.util.Map;
import java.util.Set;


public interface CReadOnlyData<K,V> extends Serializable {

    /**
     * 获得指定key对于的元素，如果指定的key对应的元素不存在将返回null
     */
    V get(K key);

    /**
     * 获得所有的Key-Val对
     */
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
