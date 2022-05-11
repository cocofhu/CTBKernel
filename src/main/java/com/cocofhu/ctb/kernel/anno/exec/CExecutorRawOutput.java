package com.cocofhu.ctb.kernel.anno.exec;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CExecutorRawOutputs.class)
public @interface CExecutorRawOutput {
    String name();
    String info();
    String type();
}