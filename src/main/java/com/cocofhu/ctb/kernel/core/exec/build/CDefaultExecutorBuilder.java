package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cocofhu
 */
public class CDefaultExecutorBuilder implements CExecutorBuilder {
    protected final CConfig config;
    protected Map<Integer, CExecutorBuilder> builderMap;

    public CDefaultExecutorBuilder(CConfig config) {
        this.config = config;
        builderMap = new HashMap<>();
        builderMap.put(CExecutorDefinition.TYPE_EXEC, new CSimpleExecutorBuilder(config));
        builderMap.put(CExecutorDefinition.TYPE_SCHEDULE, new CListExecutorBuilder(config));
        builderMap.put(CExecutorDefinition.TYPE_SVC, new CServiceExecutorBuilder(config));

    }


    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder,
                                CDefaultExecutionRuntime executionRuntime, CDefaultLayerData<String, Class<?>> contextTypes, int layer, boolean checkInput) {
        CExecutorBuilder exactlyBuilder = builderMap.get(execDetail.getType());
        if (exactlyBuilder == null) {
            throw new CExecUnsupportedOperationException("unsupported exec type: " + execDetail.getType());
        }
        return exactlyBuilder.toExecutor(execDetail, builder, executionRuntime, contextTypes, layer, checkInput);
    }


}
