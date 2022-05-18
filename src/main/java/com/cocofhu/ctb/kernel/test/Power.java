package com.cocofhu.ctb.kernel.test;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;

public class Power {


    public Power() {
    }

    @CExecutorOutputs({
            @CExecutorOutput(info = "mul result",type = Integer.class, name = CExecutionRuntime.EXEC_RETURN_VAL_KEY)
    })
    @CExecBasicInfo(name="SimpleJob",info = "info", group = "test")
    public int mul(@CExecutorInput int x, @CExecutorInput int y){
        System.out.println(x*y);
        return x*y;
    }
}
