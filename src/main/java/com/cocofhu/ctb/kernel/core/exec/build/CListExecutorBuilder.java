package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;


public class CListExecutorBuilder implements CExecutorBuilder {

    protected final CConfig config;


    public CListExecutorBuilder(CConfig config) {
        this.config = config;

    }

    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder, CDefaultExecutionRuntime executionRuntime,
                                CDefaultLayerData<String, Class<?>> contextTypes, int layer, boolean checkInput) {
        CExecutor[] executors = new CExecutor[execDetail.getSubJobs().length];
        for (int i = 0; i < execDetail.getSubJobs().length; i++) {
            executors[i] = builder.toExecutor(execDetail.getSubJobs()[i], builder, executionRuntime, contextTypes, i + layer, checkInput);
            checkInput = true;
        }
        return new CExecutorJob(executionRuntime, config, execDetail.isIgnoreException(), executors);
    }

}
