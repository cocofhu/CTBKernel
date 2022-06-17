package com.cocofhu.ctb.basic.grpc;

import com.cocofhu.ctb.kernel.anno.exec.CExecBasicInfo;
import com.cocofhu.ctb.kernel.anno.exec.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.exec.CExecutorOutput;
import com.cocofhu.ctb.kernel.core.exec.*;

import io.grpc.Server;
import io.grpc.ServerBuilder;

import java.io.IOException;


public class CGRPCBasicExecutor {

    @CExecutorOutput(info = "GRPC输出参数",type = String.class, name = "*output")
    @CExecBasicInfo(name="GRPCService",info = "标准JSON GRPC服务", group = "basic.service")
    public void service(@CExecutorInput(info = "端口号") Integer port,
                        @CExecutorInput(info = "输出参数") String output,
                        @CExecutorInput CExecutor executor) throws IOException, InterruptedException {
        Server server = ServerBuilder.forPort(port).addService(new CGRPCBasicService(executor, output)).build().start();
        server.awaitTermination();
    }

}
