package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.param.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;


public class CParamExecutor {

    public void transform(@CAutowired CExecutorContext executorContext, @CExecutorInput String source, @CExecutorInput String dist){
        Object removal = executorContext.remove(source);
        executorContext.put(dist,removal);
    }
    public void removeKey(@CAutowired CExecutorContext executorContext,@CExecutorInput String key){
        executorContext.remove(key);
    }

    public void putKey(@CAutowired CExecutorContext executorContext, @CExecutorInput String key, @CExecutorInput Object val){
        executorContext.put(key,val);
    }


}
