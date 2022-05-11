package com.cocofhu.ctb.kernel.anno.exec;

import com.cocofhu.ctb.kernel.core.exec.CExecutor;

import java.lang.annotation.ElementType;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

/**
 * @author cocofhu
 */
@Target({ElementType.TYPE,ElementType.PARAMETER})
@Retention(RetentionPolicy.RUNTIME)
public @interface CExecutorInput {
    String info() default "";
    boolean nullable() default false;

    String name() default "";
}
