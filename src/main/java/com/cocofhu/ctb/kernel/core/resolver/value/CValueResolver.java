package com.cocofhu.ctb.kernel.core.resolver.value;

import com.cocofhu.ctb.kernel.core.config.*;

import java.util.ArrayList;
import java.util.List;

/**
 * @author cocofhu
 */
public interface CValueResolver {

    List<CValueWrapper> resolveValues(CParameterWrapper parameter, CConfig context, CReadOnlyDataSet<String, Object> dataSet);
}
