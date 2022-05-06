package com.cocofhu.ctb.kernel.core.exec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author cocofhu
 */
public class CExecutorContext {

    /**
     * 执行作用域的参数
     */
    private final Map<String,Object> executionData;

    public CExecutorContext(Map<String,Object> executionData) {
        this.executionData = new ConcurrentHashMap<>();
        // 这里拷贝一份数据，避免并发时出现问题
        if(executionData != null){
            this.executionData.putAll(executionData);
        }
    }
    public CExecutorContext() {
        this(null);
    }

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

    public Object remove(String key){
        return this.put(key,null);
    }
}
