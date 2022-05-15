package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

import java.util.List;

/**
 * @author cocofhu
 */
public interface CValueResolver {

    List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig context, CReadOnlyDataSet<String, Object> dataSet) throws CBeanException;
}
