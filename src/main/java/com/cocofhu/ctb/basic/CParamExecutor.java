package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerData;


public class CParamExecutor {

    @CExecutorRawOutput(name = "*dist", info = "context output", type = "*source")
    @CExecutorContextRawInput(name = "*source", info = "context input", type = "*source")
    @CExecutorRawRemoval(name="*source", info = "will be removal", type = "*source")
    @CExecBasicInfo(name="Transform",info = "info", group = "test")
    public void transform(@CAutowired CExecutionRuntime executionRuntime, @CExecutorInput String source, @CExecutorInput String dist){
        CDefaultLayerData<String, Object> currentLayer = executionRuntime.getCurrentLayer();
        Object o = currentLayer.get(source);
        currentLayer.remove(source);
        currentLayer.put(dist,o);
    }



}
