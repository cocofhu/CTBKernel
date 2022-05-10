package com.cocofhu.ctb.kernel.core.aware;

import com.cocofhu.ctb.kernel.core.config.CConfig;

/**
 * @author cocofhu
 */
public interface CConfigAware {
    void setCTBContext(CConfig config);
}
