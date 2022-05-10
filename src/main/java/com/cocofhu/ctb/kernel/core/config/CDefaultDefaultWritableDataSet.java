package com.cocofhu.ctb.kernel.core.config;

import java.util.Set;

public class CDefaultDefaultWritableDataSet implements CDefaultDefaultReadOnlyDataSet, CWritableDataSet<String, Object> {
    @Override
    public Object get(String K) {
        return null;
    }

    @Override
    public Set<? extends CReadOnlyEntry<String, Object>> entries() {
        return null;
    }

    @Override
    public Object put(String key, Object val) {
        return null;
    }

    @Override
    public Object remove(String key) {
        return null;
    }

    @Override
    public void putAll(CReadOnlyDataSet<? extends String, ?> m) {

    }
}
