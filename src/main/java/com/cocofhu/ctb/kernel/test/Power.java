package com.cocofhu.ctb.kernel.test;

import com.cocofhu.ctb.kernel.anno.exec.CExecutorInput;
import com.cocofhu.ctb.kernel.anno.exec.CExecutorOutput;
import com.cocofhu.ctb.kernel.anno.exec.CExecutorOutputs;
import com.cocofhu.ctb.kernel.anno.exec.CJob;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;

public class Power {

    @CExecutorOutputs({
            @CExecutorOutput(info = "mul result",type = Integer.class, name = CExecutor.EXEC_RETURN_VAL_KEY)
    })
    @CJob(name="SimpleJob",info = "info", group = "test")
    public int mul(@CExecutorInput int x, @CExecutorInput int y, @CExecutorInput(nullable = true) String xxx){
        System.out.println(xxx);
        return x*y;
    }
}
