package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public interface CValueResolver {

    default List<CValueWrapper> singeValue(CValueWrapper cValueWrapper){
        List<CValueWrapper> valueWrappers = new ArrayList<>();
        valueWrappers.add(cValueWrapper);
        return valueWrappers;
    }

    List<CValueWrapper> resolveValues(CParameterWrapper parameter, CTBContext context);
}
