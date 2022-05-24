package com.cocofhu.ctb.kernel.exception.compiler;

import com.cocofhu.ctb.kernel.core.exec.compiler.CFMSExecutorCompiler;
import com.cocofhu.ctb.kernel.exception.CCompilerException;

public class CBadSyntaxException extends CCompilerException {
    public CBadSyntaxException(String msg) {
        super(msg);
    }

    public CBadSyntaxException(String msg, int pos) {
        super(msg + " at position :" + pos + ". ");
    }

    public CBadSyntaxException(String msg, int pos, String except, String but) {
        super(msg + " at position :" + pos + ", except:  " + except + ", but " + but + " found. ");
    }
}
