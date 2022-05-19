package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.job.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;

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

    }


    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder,
                                CExecutionRuntime context, CDefaultLayerDataSet<String, Class<?>> contextTypes, boolean checkInput) {
        CExecutorBuilder b = builderMap.get(execDetail.getType());
        if (b == null) {
            throw new CExecUnsupportedOperationException("unsupported exec type: " + execDetail.getType());
        }
        return b.toExecutor(execDetail, builder, context, contextTypes, checkInput);
    }


}
