package com.cocofhu.ctb.kernel.util.ds;

import com.cocofhu.ctb.kernel.util.CCloneable;

import java.util.Map;
import java.util.Set;


public interface CReadOnlyData<K, V> extends CCloneable {

    /**
     * 获得指定key对于的元素，如果指定的key对应的元素不存在将返回null
     */
    V get(K key);

    /**
     * 获得所有的Key-Val对
     */
    Set<? extends CReadOnlyEntry<K, V>> entries();


    interface CReadOnlyEntry<K, V> {
        K getKey();

        V getValue();
    }

    /**
     * 转化成Map
     */
    Map<K, V> toReadOnlyMap();

}
