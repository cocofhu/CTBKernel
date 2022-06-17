package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;


public class CServiceExecutorBuilder implements CExecutorBuilder {

    protected final CConfig config;

    public CServiceExecutorBuilder(CConfig config) {
        this.config = config;
    }


    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder,
                                CWritableData<String, Class<?>> contextTypes,
                                CWritableData<String, Object> attachedValues,
                                String layer, boolean checkInput) {

        CExecutorDefinition self = (CExecutorDefinition) execDetail.cloneSelf();
        self.setType(self.getSubJobs() == null ? CExecutorDefinition.TYPE_EXEC : CExecutorDefinition.TYPE_SCHEDULE);
        contextTypes.put("executor", CExecutor.class);
        // 服务启动
        CExecutor service = builder.toExecutor(self.getInitExecution(), builder, contextTypes, attachedValues, "service", true);
        // 服务处理器
        CExecutor executor = builder.toExecutor(self, builder, contextTypes, attachedValues, "handler", true);
        return new CServiceExecutor(config, executor, service);
    }

}
