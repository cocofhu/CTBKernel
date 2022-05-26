package com.cocofhu.ctb.kernel.core.exec.compiler;


import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;


import java.util.function.BiFunction;


public interface CExecutorCompiler {
    CExecutorDefinition compiler(String expression, int flag);

}
