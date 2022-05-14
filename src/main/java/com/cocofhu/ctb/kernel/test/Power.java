package com.cocofhu.ctb.kernel.test;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;

public class Power {

    @CExecutorOutputs({
            @CExecutorOutput(info = "mul result",type = String.class, name = "x")
    })
    public Power() {
    }

    @CExecutorOutputs({
            @CExecutorOutput(info = "mul result",type = Integer.class, name = CExecutor.EXEC_RETURN_VAL_KEY)
    })
//    @CExecutorContextInputs(
//            @CExecutorContextInput(info = "mul result",type = String.class, name = "x")
//    )
    @CExecutorContextInput(info = "mul result",type = String.class, name = "k1")
    @CExecutorContextRawInput(info = "mul result",type = "x", name = "k3")
    @CExecutorContextRawInput(info = "mul result",type = "x", name = "k5")

//    @CExecutorContextInput(info = "mul result",type = String.class, name = "k2")
    @CExecBasicInfo(name="SimpleJob",info = "info", group = "test")
    public int mul(@CExecutorInput int x, @CExecutorInput int y, @CExecutorInput(nullable = true) String xxx){
        System.out.println(xxx);
        return x*y;
    }
}
