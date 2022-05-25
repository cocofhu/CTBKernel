package com.cocofhu.ctb.kernel.exception.compiler;

import com.cocofhu.ctb.kernel.exception.CCompilerException;

public class CBadSyntaxException extends CCompilerException {

    private final String sourceCode;

    public CBadSyntaxException(String sourceCode, String msg) {
        super(msg);
        this.sourceCode = sourceCode;
    }

    public CBadSyntaxException(String sourceCode, String msg, int pos) {
        this(sourceCode, msg + " at position :" + pos + ". ");
    }

    public CBadSyntaxException(String sourceCode, String msg, int pos, String unexpected) {
        this(sourceCode, msg + " at position :" + pos + ", unexpected: '" + unexpected + "'. ");
    }

    public CBadSyntaxException(String sourceCode, String msg, int pos, String expected, String unexpected) {
        this(sourceCode, msg + " at position :" + pos + ", except:  " + expected + ", but '" + unexpected + "' found. ");
    }

    public String getSourceCode() {
        return sourceCode;
    }
}
