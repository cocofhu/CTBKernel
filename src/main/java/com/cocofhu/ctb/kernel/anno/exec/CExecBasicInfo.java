package com.cocofhu.ctb.kernel.anno.exec;


import java.lang.annotation.*;

@Target({ElementType.METHOD})
@Retention(RetentionPolicy.RUNTIME)
public @interface CExecBasicInfo {
    String name();
    String info();
    String group();
    boolean ignoreException() default false;
}