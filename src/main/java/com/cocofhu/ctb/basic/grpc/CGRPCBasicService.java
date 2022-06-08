package com.cocofhu.ctb.basic.grpc;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import io.grpc.stub.StreamObserver;

public class CGRPCBasicService extends CGRPCBasicServiceGrpc.CGRPCBasicServiceImplBase{

    private final CExecutor executor;

    public CGRPCBasicService(CExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(CGRPCBasicProto.GRPCBasicData request, StreamObserver<CGRPCBasicProto.GRPCBasicData> responseObserver) {

        try {
            String data = request.getData();
            executor.setStatus(CExecutor.Status.Ready);
            CDefaultWritableData<String, Object> newAttachment = new CDefaultWritableData<>();
            newAttachment.put("grpcData", data);
            CDefaultExecutionRuntime runtime = CDefaultExecutionRuntime.newDefault();
            runtime.start(newAttachment, CExecutionRuntime.CExecutorRuntimeType.SERVICE, executor);
            executor.run(runtime);
            String value = JSON.toJSONString(runtime.getReturnVal());
            responseObserver.onNext(CGRPCBasicProto.GRPCBasicData.newBuilder().setData(value).build());
        }finally {
            responseObserver.onCompleted();
        }
    }
}
