package com.cocofhu.ctb.kernel.exception;

/**
 * 任务链中参数不匹配
 * @author cocofhu
 */
public class CJobParamNotMatchException extends CNestedRuntimeException {
    public CJobParamNotMatchException(String msg) {
        super(msg);
    }
}
