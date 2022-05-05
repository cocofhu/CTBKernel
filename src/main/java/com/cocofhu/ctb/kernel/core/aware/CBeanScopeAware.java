package com.cocofhu.ctb.kernel.core.aware;

import com.cocofhu.ctb.kernel.core.config.CDefinition;

/**
 * @author cocofhu
 */
public interface CBeanScopeAware {
    void setScope(CDefinition.CBeanScope scope);
}
