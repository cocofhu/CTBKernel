package com.cocofhu.ctb.kernel.core.exec;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.util.ds.CDefaultReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;

/**
 * @author cocofhu
 */
public class CServiceExecutor extends CAbstractExecutor {


    private final CExecutor executor;
    private final CExecutor service;

    public CServiceExecutor(CDefaultExecutionRuntime executionRuntime, CConfig config, CExecutor executor, CExecutor service) {
        super(executionRuntime, null, config);
        this.executor = executor;
        this.service = service;
    }


    @Override
    public void run() {
        System.out.println(executor.getExecutorDefinition());
        CDefaultReadOnlyData<String, Object> attachment = service.getExecutorDefinition().getAttachment();
        CDefaultWritableData<String, Object> newAttachment = new CDefaultWritableData<>(attachment);
        newAttachment.put("executor", executor);
        service.getExecutorDefinition().setAttachment(newAttachment);
        new Thread(service).start();
        System.out.println("GRPC SERVICE START!");
    }


}
