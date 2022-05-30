package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.exception.exec.CExecParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.CCloneable;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;


public class CServiceExecutorBuilder extends CSimpleExecutorBuilder {


    public CServiceExecutorBuilder(CConfig config) {
        super(config);
    }


    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder,
                                CDefaultLayerData<String, Class<?>> contextTypes, String layer, boolean checkInput) {

        CExecutorDefinition self = (CExecutorDefinition) execDetail.cloneSelf();
        self.setType(self.getSubJobs() == null ? CExecutorDefinition.TYPE_EXEC : CExecutorDefinition.TYPE_SCHEDULE);
        contextTypes.put("executor", CExecutor.class);
        // 服务启动
        CExecutor service = builder.toExecutor(self.getInitExecution(), builder, contextTypes, "0", false);
        // 服务处理器
        CExecutor executor = builder.toExecutor(self, builder, contextTypes,  "1", true);
        return new CServiceExecutor(config, executor, service);
    }

}
