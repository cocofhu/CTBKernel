package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.*;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

/**
 *
 * @author cocofhu
 */
public class CExecutorContext extends CDefaultDefaultWritableDataSet implements CDefaultLayerDataSet {


    @Override
    public CExecutorContext newLayer() {
        return null;
    }

    @Override
    public void backLayer(int layer) {

    }
}
