package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.exception.CExecException;

import java.util.Arrays;
import java.util.List;

/**
 * 任务链中参数不匹配
 *
 * @author cocofhu
 */
public class CExecParamNotFoundException extends CExecException {
    public CExecParamNotFoundException(String msg) {
        super(msg);
    }

    public CExecParamNotFoundException(CPair<String, Class<?>> required, List<CParameterDefinition> candidates, int layer) {
        super("parameter not match in this job at layer " + layer + ", " +
                "require ( type: " + required.getSecond() + ", name: " + required.getFirst() + "), " +
                "but found(s) " + Arrays.toString(candidates.stream().map(param -> "( type: " + param.getType() + ", name: " + param.getName() + ")").toArray(String[]::new)));
    }
}
