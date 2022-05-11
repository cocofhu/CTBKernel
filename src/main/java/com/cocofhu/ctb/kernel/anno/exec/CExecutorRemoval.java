package com.cocofhu.ctb.kernel.anno.exec;


import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CExecutorRemovals.class)
public @interface CExecutorRemoval {
    String name();
    String info();
    Class<?> type();
}