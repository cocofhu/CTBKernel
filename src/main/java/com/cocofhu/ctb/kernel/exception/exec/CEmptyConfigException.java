package com.cocofhu.ctb.kernel.exception.exec;

import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.exception.CResolverException;

/**
 * 空配置异常
 * @author cocofhu
 */
public class CEmptyConfigException extends CResolverException {
    public CEmptyConfigException() {
        super("empty factory config.");
    }
}
