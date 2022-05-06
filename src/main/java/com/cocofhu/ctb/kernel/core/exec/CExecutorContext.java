package com.cocofhu.ctb.kernel.core.exec;

import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

/**
 * @author cocofhu
 */
public class CExecutorContext {

    private final Map<String,Object> executionData = new ConcurrentHashMap<>();
    private final CExecutorContext attachment = new CExecutorContext();

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

    public void newAttachment(Map<String,Object> attachment){
        this.attachment.executionData.clear();
        this.attachment.executionData.putAll(attachment);
    }

    public Object getAttachment(String key) {
        return attachment.get(key);
    }
}
