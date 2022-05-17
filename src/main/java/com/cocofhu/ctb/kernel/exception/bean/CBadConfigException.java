package com.cocofhu.ctb.kernel.exception.bean;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.exception.CExecException;

/**
 * 配置对象出现错误，部分非空字段为空等
 * @author cocofhu
 */
public class CBadConfigException extends CExecException {
    private final CConfig config;
    public CBadConfigException(CConfig config, String msg) {
        super(msg);
        this.config = config;
    }

    public CConfig getConfig() {
        return config;
    }
}
