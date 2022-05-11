package com.cocofhu.ctb.kernel.anno.exec;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CExecutorContextRawInputs.class)
public @interface CExecutorContextRawInput {
    String name();
    String info();
    String type();
}