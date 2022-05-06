package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.CAttachmentArgs;
import com.cocofhu.ctb.kernel.anno.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;

public class CDebugExecutor {
    public void println(@CAutowired CExecutorContext executorContext, @CAttachmentArgs String target){
        System.out.println(executorContext.get(target));
    }
}
