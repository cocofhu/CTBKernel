package com.cocofhu.ctb.kernel.anno.exec;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
public @interface CExecutorRawOutputs {
    CExecutorRawOutput[] value();

}