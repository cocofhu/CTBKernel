package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.exception.CJobException;

import java.util.Arrays;

/**
 * 没有找到指定的方法
 * @author cocofhu
 */
public class CNoSuchMethodException extends CJobException {
    public CNoSuchMethodException(String methodName, Class<?>... parameterTypes) {
        super("no such method of " + methodName + "(" + Arrays.toString(parameterTypes) + ")");
    }
}
