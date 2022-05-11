package com.cocofhu.ctb.kernel.anno.exec;




import java.lang.annotation.*;

@Target({ElementType.METHOD,ElementType.CONSTRUCTOR})
@Retention(RetentionPolicy.RUNTIME)
@Repeatable(CExecutorOutputs.class)
public @interface CExecutorOutput {
    String name();
    String info();
    Class<?> type();
}