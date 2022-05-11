package com.cocofhu.ctb.kernel.anno.exec;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CExecutorRawRemovals.class)
public @interface CExecutorRawRemoval {
    String name();
    String info();
    String type();
}