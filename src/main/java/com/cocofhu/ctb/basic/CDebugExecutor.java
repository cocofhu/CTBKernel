package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;

import java.sql.Connection;

import static com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime.EXEC_RETURN_VAL_KEY;

public class CDebugExecutor {

    @CExecBasicInfo(name="Debug",info = "info", group = "test")
    @CExecutorOutput(info = "mul result",type = String.class, name = EXEC_RETURN_VAL_KEY)
    public String debug(@CAutowired CDefaultExecutionRuntime runtime){
        System.out.println(runtime);
        return "[DEBUG] " + runtime.getReturnVal();
    }
}
