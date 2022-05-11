package com.cocofhu.ctb.kernel.anno.exec;


import com.cocofhu.ctb.kernel.core.exec.CExecutor;

import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CExecutorContextInputs.class)
public @interface CExecutorContextInput {
    String name();
    String info();
    Class<?> type();
}