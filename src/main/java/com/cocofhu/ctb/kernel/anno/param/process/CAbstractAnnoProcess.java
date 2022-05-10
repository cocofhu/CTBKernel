package com.cocofhu.ctb.kernel.anno.param.process;

import com.cocofhu.ctb.kernel.core.config.*;

/**
 * 将参数进行分散，保证代码的可读性的注解处理器
 * @author cocofhu
 */
public abstract class CAbstractAnnoProcess implements CAnnoProcess {
    @Override
    public CPair<Object, Boolean> process(CPair<CParameterWrapper, CDefaultDefaultReadOnlyDataSet> target, CConfig config) {
        return process(target.getFirst(), config, target.getSecond());
    }

    public abstract CPair<Object, Boolean> process(CParameterWrapper parameterWrapper, CConfig config, CDefaultDefaultReadOnlyDataSet dataSet);

}
