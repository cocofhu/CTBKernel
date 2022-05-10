package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.core.config.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.config.CParameterWrapper;
import com.cocofhu.ctb.kernel.core.config.CPair;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;

/**
 * 参数注解处理器
 * CParameterWrapper：                   参数
 * CDefaultDefaultReadOnlyDataSet：      附加的参数，通过使用该参数，配合不同的处理器，可以完成一些自定义的处理
 * @author cocofhu
 */
public interface CAnnoProcess extends CProcess<CPair<CParameterWrapper, CDefaultDefaultReadOnlyDataSet>> {
}
