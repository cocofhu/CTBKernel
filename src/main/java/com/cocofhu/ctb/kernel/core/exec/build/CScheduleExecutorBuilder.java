package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;


public class CScheduleExecutorBuilder implements CExecutorBuilder {

    protected final CConfig config;


    public CScheduleExecutorBuilder(CConfig config) {
        this.config = config;

    }

    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder, CExecutionRuntime context,
                                CDefaultLayerDataSet<String, Class<?>> contextTypes, boolean checkInput) {
        CExecutor[] executors = new CExecutor[execDetail.getSubJobs().length];
        for (int i = 0; i < execDetail.getSubJobs().length; i++) {
            executors[i] = builder.toExecutor(execDetail.getSubJobs()[i], builder, context, contextTypes, checkInput);
            checkInput = true;
        }
        return new CExecutorJob(context, config, execDetail.isIgnoreException(), executors);
    }

}
