package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.exec.*;
import com.cocofhu.ctb.kernel.anno.param.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Properties;

import static com.cocofhu.ctb.kernel.core.exec.CExecutionRuntime.EXEC_RETURN_VAL_KEY;


public class CUtilExecutor {



    @CExecutorOutput(info = "Properties",type = Properties.class, name = "*output")
    @CExecBasicInfo(name="CSinkProperties",info = "读取配置文件", group = "basic.util")
    public void sinkProperties(@CExecutorInput(info = "配置文件路径") String source,
                               @CExecutorInput(info = "配置类输出的变量名字") String output,
                               @CAutowired CExecutionRuntime runtime) throws IOException {
        Properties properties =  new Properties();
        try (InputStream is = Files.newInputStream(Paths.get(source))){
            properties.load(is);
        }
        runtime.put(output, properties);
    }

    @CExecBasicInfo(name="CDebug",info = "Debug,输出所有上下文的变量", group = "basic.util")
    public void debug(@CAutowired CDefaultExecutionRuntime runtime){
        System.out.println(runtime.toReadOnlyMap());
    }

}
