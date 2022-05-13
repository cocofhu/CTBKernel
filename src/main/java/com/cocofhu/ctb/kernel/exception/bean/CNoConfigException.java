package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.exception.CBeanException;

/**
 * 空配置异常
 * @author cocofhu
 */
public class CNoConfigException extends CBeanException {
    public CNoConfigException() {
        super("empty bean factory config.");
    }
}
