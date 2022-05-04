package com.cocofhu.ctb.kernel.core.factory.exec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CExecutorContext {

    private final Map<String,Object> executionData = new ConcurrentHashMap<>();

    public Object get(String key){
        return executionData.get(key);
    }
    public Object put(String key,Object val){
        if(key == null) {
            return null;
        }
        if(val == null) {
            return executionData.remove(key);
        }
        return executionData.put(key,val);
    }

}
