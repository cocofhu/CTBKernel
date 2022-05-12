package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CBeanException;

import java.util.Arrays;

/**
 * 没有找到指定的方法
 * @author cocofhu
 */
public class CNoSuchMethodException extends CBeanException {
    public CNoSuchMethodException(String methodName, Class<?>... parameterTypes) {
        super("no such method of " + methodName + "(" + Arrays.toString(parameterTypes) + ")");
    }
}
