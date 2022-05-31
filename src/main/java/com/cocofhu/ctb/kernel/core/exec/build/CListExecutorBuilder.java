package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;



public class CListExecutorBuilder implements CExecutorBuilder {

    protected final CConfig config;


    public CListExecutorBuilder(CConfig config) {
        this.config = config;

    }

    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder,
                                CWritableData<String, Class<?>> contextTypes,
                                CWritableData<String, Object> attachedValues,
                                String layer, boolean checkInput) {

        attachedValues = new CDefaultWritableData<>(attachedValues);
        attachedValues.putAll(execDetail.getAttachment());
        CExecutor[] executors = new CExecutor[execDetail.getSubJobs().length];
        for (int i = 0; i < execDetail.getSubJobs().length; i++) {
            executors[i] = builder.toExecutor(execDetail.getSubJobs()[i], builder, contextTypes, attachedValues, layer + "-" + i, checkInput);
            checkInput = true;
        }
        return new CListExecutor(config, execDetail.isIgnoreException(), execDetail, executors);
    }



}
