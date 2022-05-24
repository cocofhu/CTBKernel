package com.cocofhu.ctb.kernel.core.exec.compiler;


import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.compiler.CBadSyntaxException;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;


import java.util.function.BiFunction;


public interface CExecutorCompiler {
    CExecutorDefinition compiler(String expression, int flag);

}
