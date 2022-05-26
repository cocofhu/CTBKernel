package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.exception.exec.CExecParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.CCloneable;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;
import com.cocofhu.ctb.kernel.util.ds.CPair;

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
                                CDefaultLayerData<String, Class<?>> contextTypes, int layer, boolean checkInput) {

        // layer must equal zero

        CExecutorDefinition self1 = (CExecutorDefinition) execDetail.cloneSelf();
        CExecutorDefinition self2 = (CExecutorDefinition) execDetail.cloneSelf();
        self1.setType(CExecutorDefinition.TYPE_EXEC);
        self2.setType(CExecutorDefinition.TYPE_SCHEDULE);
        CExecutor service = builder.toExecutor(self1, builder, contextTypes, 0, false);
        CExecutor executor = builder.toExecutor(self2, builder, contextTypes, 1, true);
        return new CServiceExecutor(config,executor,service);
    }

}
