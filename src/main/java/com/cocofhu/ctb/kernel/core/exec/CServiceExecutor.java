package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.util.ds.CDefaultReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;

/**
 * @author cocofhu
 */
public class CServiceExecutor extends CAbstractExecutor {


    private final CExecutor executor;
    private final CExecutor service;

    public CServiceExecutor(CConfig config, CExecutor executor, CExecutor service) {
        super(null, config);
        this.executor = executor;
        this.service = service;
    }


    private void init(CExecutionRuntime runtime){
        CWritableData<String,Object> attachment = new CDefaultWritableData<>();
        attachment.put("executor", executor);
        runtime.startNew(attachment, CExecutionRuntime.CExecutorRuntimeType.ARGS_COPY, this);
    }

    @Override
    public void run(CExecutionRuntime runtime) {
        init(runtime);
        new Thread(()-> service.run(runtime)).start();
    }


}
