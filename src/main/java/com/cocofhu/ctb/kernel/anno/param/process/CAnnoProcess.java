package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

/**
 * @author cocofhu
 */
public interface CAnnoProcess {
    CPair<Object, Boolean> process(CParameterWrapper parameterWrapper, CConfig config, CReadOnlyDataSet<String, Object> dataSet);

}
