package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public class CChainValueResolver implements CValueResolver {

    private final CValueResolver[] resolvers;

    public CChainValueResolver(CValueResolver[] resolvers) {
        if (resolvers == null) {
            resolvers = new CValueResolver[0];
        }
        this.resolvers = resolvers;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet) {
        List<CValueWrapper> values = new ArrayList<>();
        for (CValueResolver resolver : resolvers) {
            List<CValueWrapper> cValueWrappers = resolver.resolveValues(parameter, config, dataSet);
            if (cValueWrappers != null) {
                values.addAll(cValueWrappers);
            }
        }
        return values;
    }
}
