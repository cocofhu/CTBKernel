package com.cocofhu.ctb.basic.grpc;

import com.cocofhu.ctb.kernel.anno.exec.CExecBasicInfo;
import com.cocofhu.ctb.kernel.anno.exec.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.exec.CExecutorOutput;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.*;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;
import java.util.Map;
import java.util.UUID;


public class CGRPCBasicExecutor {

    @CExecutorOutput(info = "mul result",type = String.class, name = "grpcData")
    @CExecBasicInfo(name="GRPCService",info = "info", group = "test")
    public void service(@CExecutorInput Integer port, @CExecutorInput CExecutor executor) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(port).addService(new CGRPCBasicService(executor)).build().start();
        server.awaitTermination();
    }

}
