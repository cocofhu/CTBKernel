package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;
import lombok.extern.slf4j.Slf4j;

/**
 * @author cocofhu
 */
@Slf4j
public class CServiceExecutor extends CAbstractExecutor {


    private final CExecutor executor;
    private final CExecutor service;

    public CServiceExecutor(CConfig config, CExecutor executor, CExecutor service) {
        super(null, config);
        this.executor = executor;
        this.service = service;
    }

    @Override
    public void run(CExecutionRuntime runtime) {
        CWritableData<String,Object> attachment = new CDefaultWritableData<>();
        attachment.put("executor", executor);
        CExecutionRuntime service = runtime.start(attachment, CExecutionRuntime.CExecutorRuntimeType.SIMPLE, this);
        Thread thread = new Thread(() -> this.service.run(service));
        thread.start();
        service.finish();
        log.info("Service started, TID:" + thread.getId());
    }


}
