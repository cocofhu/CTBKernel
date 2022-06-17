package com.cocofhu.ctb.basic.grpc;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import io.grpc.stub.StreamObserver;

public class CGRPCBasicService extends CGRPCBasicServiceGrpc.CGRPCBasicServiceImplBase{

    private final CExecutor executor;
    private final String targetName;
    public CGRPCBasicService(CExecutor executor, String targetName) {
        this.executor = executor;
        this.targetName = targetName;
    }

    @Override
    public void execute(CGRPCBasicProto.GRPCBasicData request, StreamObserver<CGRPCBasicProto.GRPCBasicData> responseObserver) {

        try {
            String data = request.getData();
            executor.setStatus(CExecutor.Status.Ready);
            CDefaultWritableData<String, Object> newAttachment = new CDefaultWritableData<>();
            newAttachment.put(targetName, data);
            CDefaultExecutionRuntime runtime = CDefaultExecutionRuntime.newRoot();
            CExecutionRuntime next = runtime.start(newAttachment, CExecutionRuntime.CExecutorRuntimeType.SERVICE, executor);
            executor.run(next);
            String value = JSON.toJSONString(next.getReturnVal());
            responseObserver.onNext(CGRPCBasicProto.GRPCBasicData.newBuilder().setData(value).build());
        }finally {
            responseObserver.onCompleted();
        }
    }
}
