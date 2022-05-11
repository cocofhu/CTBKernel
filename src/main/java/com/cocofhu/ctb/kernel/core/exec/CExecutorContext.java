package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.*;

import java.util.HashMap;

/**
 *
 * @author cocofhu
 */
public class CExecutorContext extends CDefaultDefaultWritableDataSet implements CDefaultLayerDataSet {

    private final CExecutorContext parent;

    private CExecutorContext(CExecutorContext parent) {
        // 除了最顶层，其他的可以使用HashMap提升效率
        // 这也就意味着，除了最顶层都不是线程安全的数据实现
        super(new HashMap<>(), null);
        this.parent = parent;
    }

    public CExecutorContext() {
        this(null);
    }

    @Override
    public CExecutorContext newLayer() {
        return new CExecutorContext(this);
    }

    @Override
    public CExecutorContext getParent() {
        return this.parent;
    }

    @Override
    public Object get(String key) {
        Object o = super.get(key);
        CExecutorContext p = this.getParent();
        return o == null && p != null ? p.get(key) : o;
    }
}
