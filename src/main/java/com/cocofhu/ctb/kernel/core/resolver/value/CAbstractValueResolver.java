package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.convert.ConverterUtils;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

import java.util.List;

public abstract class CAbstractValueResolver implements CValueResolver, CProcess<CParameterWrapper> {

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CTBContext context) {
        CTBPair<Object, Boolean> pair = process(parameter, context);
        if (pair != null && pair.getSecond()) {
            return singeValue(new CValueWrapper(this, pair, context));
        }
        return null;
    }
}
