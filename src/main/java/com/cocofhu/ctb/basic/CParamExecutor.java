package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;


public class CParamExecutor {

    @CExecutorRawRemovals(
            @CExecutorRawRemoval(name="*source", info = "will be removal", type = "*source")
    )
    @CExecutorContextRawInputs(
            @CExecutorContextRawInput(name = "*source", info = "context input", type = "*source")
    )
    @CExecutorRawOutputs(
            @CExecutorRawOutput(name = "*dist", info = "context output", type = "*source")
    )
    @CExecBasicInfo(name="SimpleJob",info = "info", group = "test")
    public void transform(@CAutowired CExecutionRuntime executorContext, @CExecutorInput String source, @CExecutorInput String dist){
        CDefaultLayerDataSet<String, Object> currentLayer = executorContext.getCurrentLayer();
        Object o = currentLayer.get(source);
        currentLayer.remove(source);
        currentLayer.put(dist,o);


    }



}
