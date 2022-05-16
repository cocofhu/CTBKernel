package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecDetail;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecParam;
import com.cocofhu.ctb.kernel.exception.job.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;
import com.cocofhu.ctb.kernel.util.ds.CPair;

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
        builderMap.put(CExecDetail.TYPE_EXEC, new CExecExecutorBuilder(config));
        builderMap.put(CExecDetail.TYPE_SCHEDULE, new CScheduleExecutorBuilder(config));

    }


    @Override
    public CPair<CExecutor, CExecParam[]> toExecutor(CExecDetail execDetail, CExecutorBuilder builder,
                                                     CExecutorContext context, CDefaultLayerDataSet<String, Class<?>> contextTypes, CExecParam[] lastOutput, boolean checkInput) {
        CExecutorBuilder b = builderMap.get(execDetail.getType());
        if (b == null) {
            throw new CExecUnsupportedOperationException("unsupported exec type: " + execDetail.getType());
        }
        return b.toExecutor(execDetail, builder, context, contextTypes, lastOutput, checkInput);
    }


}
