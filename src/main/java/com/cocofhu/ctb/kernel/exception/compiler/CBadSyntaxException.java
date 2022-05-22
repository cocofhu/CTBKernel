package com.cocofhu.ctb.kernel.exception.compiler;

import com.cocofhu.ctb.kernel.exception.CCompilerException;

public class CBadSyntaxException extends CCompilerException {
    public CBadSyntaxException(String msg) {
        super(msg);
    }
}
