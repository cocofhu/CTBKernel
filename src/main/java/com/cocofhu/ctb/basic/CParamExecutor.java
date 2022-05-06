package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.CAttachmentArgs;
import com.cocofhu.ctb.kernel.anno.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;


public class CParamExecutor {

    public void transform(@CAutowired CExecutorContext executorContext, @CAttachmentArgs String source, @CAttachmentArgs String dist){
        Object removal = executorContext.remove(source);
        executorContext.put(dist,removal);
    }
    public void removeKey(@CAutowired CExecutorContext executorContext,@CAttachmentArgs String key){
        executorContext.remove(key);
    }

    public void putKey(@CAutowired CExecutorContext executorContext, @CAttachmentArgs String key, @CAttachmentArgs Object val){
        executorContext.put(key,val);
    }


}
