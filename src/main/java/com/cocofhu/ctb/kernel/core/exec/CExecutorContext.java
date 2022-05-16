package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;

/**
 *
 * @author cocofhu
 */
public class CExecutorContext extends CDefaultLayerDataSet<String,Object> {

    private CExecutorContext parent;

    private CExecutorContext(CExecutorContext parent) {
        this.parent = parent;
    }
    public CExecutorContext(){
        this(null);
    }

    public Object persist(String key, Object val) {
        if(getParent() == null) {
            return null;
        }
        return getParent().put(key, val);
    }
    public Object cancelPersist(String key) {
        if(getParent() == null) {
            return null;
        }
        return getParent().remove(key);
    }

    @Override
    public CExecutorContext getParent() {
        return parent;
    }

    @Override
    public CExecutorContext newLayer() {
        return new CExecutorContext(this);
    }
}
