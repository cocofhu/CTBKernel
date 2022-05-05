package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.config.CValueWrapper;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public class CChainValueResolver implements CValueResolver{

    private final CValueResolver[] resolvers;

    public CChainValueResolver(CValueResolver[] resolvers) {
        if(resolvers == null){
            resolvers = new CValueResolver[0];
        }
        this.resolvers = resolvers;
    }

    @Override
    public List<CValueWrapper> resolveValues(CParameterWrapper parameter, CTBContext context) {
        List<CValueWrapper> values = new ArrayList<>();
        for (CValueResolver resolver : resolvers) {
            List<CValueWrapper> cValueWrappers = resolver.resolveValues(parameter, context);
            if (cValueWrappers != null) {
                values.addAll(cValueWrappers);
            }
        }
        return values;
    }
}
