package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;

public class CDebugExecutor {
    public void println(@CAutowired CExecutionRuntime executorContext, @CExecutorInput String target){
//        System.out.println(executorContext.get(target));
    }
}
