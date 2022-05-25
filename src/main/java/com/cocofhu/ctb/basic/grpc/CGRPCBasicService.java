package com.cocofhu.ctb.basic.grpc;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.util.ds.CDefaultReadOnlyData;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import io.grpc.stub.StreamObserver;

public class CGRPCBasicService extends CGRPCBasicServiceGrpc.CGRPCBasicServiceImplBase{

    private final CExecutor executor;

    public CGRPCBasicService(CExecutor executor) {
        this.executor = executor;
    }

    @Override
    public void execute(CGRPCBasicProto.GRPCBasicData request, StreamObserver<CGRPCBasicProto.GRPCBasicData> responseObserver) {
        String data = request.getData();
        try {
            executor.setStatus(CExecutor.Status.Ready);
            CDefaultReadOnlyData<String, Object> attachment = executor.getExecutorDefinition().getAttachment();
            CDefaultWritableData<String, Object> newAttachment = new CDefaultWritableData<>(attachment);
            newAttachment.put("grpcData", data);
            executor.getExecutorDefinition().setAttachment(newAttachment);
            executor.run();
            String value = JSON.toJSONString(executor.getReturnVal());
            responseObserver.onNext(CGRPCBasicProto.GRPCBasicData.newBuilder().setData(value).build());
        }finally {
            responseObserver.onCompleted();
        }
    }
}
