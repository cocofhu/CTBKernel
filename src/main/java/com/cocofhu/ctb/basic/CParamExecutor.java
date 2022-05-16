package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;


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
    public void transform(@CAutowired CExecutorContext executorContext, @CExecutorInput String source, @CExecutorInput String dist){
        Object removal = executorContext.cancelPersist(source);
        executorContext.persist(dist,removal);
    }
    public void removeKey(@CAutowired CExecutorContext executorContext,@CExecutorInput String key){
        executorContext.cancelPersist(key);
    }

    public void putKey(@CAutowired CExecutorContext executorContext, @CExecutorInput String key, @CExecutorInput Object val){
        executorContext.persist(key,val);
    }


}
