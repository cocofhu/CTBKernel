package com.cocofhu.ctb.kernel.exception;

import java.util.Arrays;

/**
 * 没有找到指定的方法
 * @author cocofhu
 */
public class CNoSuchMethodException extends CNestedRuntimeException {
    public CNoSuchMethodException(String methodName, Class<?>... parameterTypes) {
        super("no such method of " + methodName + "(" + Arrays.toString(parameterTypes) + ")");
    }
}
