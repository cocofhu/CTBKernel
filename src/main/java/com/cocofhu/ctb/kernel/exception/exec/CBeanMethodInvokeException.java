package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 执行Bean的方法时出错
 * @author cocofhu
 */
public class CBeanMethodInvokeException extends CBeanException {
    public CBeanMethodInvokeException(String msg) {
        super(msg);
    }
}
