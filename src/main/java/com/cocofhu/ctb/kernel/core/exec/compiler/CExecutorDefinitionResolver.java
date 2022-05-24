package com.cocofhu.ctb.kernel.core.exec.compiler;

import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;

public interface CExecutorDefinitionResolver {
    CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias);
}
