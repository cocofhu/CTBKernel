package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

import java.util.List;

/**
 * @author cocofhu
 */
public abstract class CAbstractValueResolver implements CValueResolver, CProcess<CParameterWrapper> {

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CTBContext context) {
        CTBPair<Object, Boolean> pair = process(parameter, context);
        if (pair != null && pair.getSecond()) {
            return singeValue(new CValueWrapper(this, pair, context, parameter));
        }
        return null;
    }
}
