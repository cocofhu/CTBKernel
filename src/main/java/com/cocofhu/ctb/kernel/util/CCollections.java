package com.cocofhu.ctb.kernel.util;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class CCollections {

    @SafeVarargs
    public static <T> List<T> toList(T... arr){
        List<T> list = new ArrayList<>();
        if(!empty(arr)){
            Collections.addAll(list, arr);
        }
        return list;
    }

    public static <T> boolean empty(T[] arr){
        return arr == null || arr.length == 0;
    }

}
