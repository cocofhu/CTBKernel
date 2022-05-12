package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 不支持的Executable类型
 * @author cocofhu
 */
public class CUnsupportedExecutableTypeException extends CBeanException {
    private final CExecutableWrapper executor;
    public CUnsupportedExecutableTypeException(CExecutableWrapper executor) {
        super("unsupported executable type of " + executor.getExecutor().getClass());
        this.executor = executor;
    }

    public CExecutableWrapper getExecutor() {
        return executor;
    }
}
