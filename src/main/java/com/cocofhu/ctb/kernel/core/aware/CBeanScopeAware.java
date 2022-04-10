package com.cocofhu.ctb.kernel.core.aware;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;

public interface CBeanScopeAware {
    void setScope(CBeanDefinition.CBeanScope scope);
}
