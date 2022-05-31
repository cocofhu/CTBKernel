package com.cocofhu.ctb.kernel.test;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;

public class Power {


    public Power() {
    }

    @CExecutorOutput(info = "mul result",type = Integer.class, name = CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY)
    @CExecBasicInfo(name="SimpleJob",info = "info", group = "test")
    public int mul(@CExecutorInput Integer x, @CExecutorInput Integer y){
        System.out.println("POWER: "+x*y);
        return x*y;
    }
}
