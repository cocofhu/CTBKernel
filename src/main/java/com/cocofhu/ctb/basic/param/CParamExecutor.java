package com.cocofhu.ctb.basic.param;

import com.cocofhu.ctb.kernel.anno.CExecutorContextPrefix;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;

@CExecutorContextPrefix("#CParamExecutor_")
public class CParamExecutor {

    public void transform(CExecutorContext executorContext,String source, String dist){
        Object removal = executorContext.remove(source);
        executorContext.put(dist,removal);
    }
    public void removeKey(CExecutorContext executorContext,@CExecutorContextPrefix("")String key){
        executorContext.remove(key);
    }

    public void putKey(CExecutorContext executorContext,@CExecutorContextPrefix("")String key,@CExecutorContextPrefix("")Object val){
        executorContext.put(key,val);
    }

    public void output(@CExecutorContextPrefix("") Object out){
        System.out.println(out);
    }
    public void outputAll(CExecutorContext executorContext){
        System.out.println(executorContext);
    }


}
