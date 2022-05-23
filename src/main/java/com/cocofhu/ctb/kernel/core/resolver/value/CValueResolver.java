package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

import java.util.List;

/**
 * @author cocofhu
 */
public interface CValueResolver {

    List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig context, CReadOnlyData<String, Object> data) throws CBeanException;
}
