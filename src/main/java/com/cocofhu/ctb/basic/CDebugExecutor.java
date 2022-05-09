package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.param.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;

public class CDebugExecutor {
    public void println(@CAutowired CExecutorContext executorContext, @CExecutorInput String target){
        System.out.println(executorContext.get(target));
    }
}
