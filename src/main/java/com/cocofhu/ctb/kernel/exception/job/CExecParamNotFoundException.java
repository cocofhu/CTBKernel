package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.entity.CExecParam;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.exception.CJobException;

import java.util.Arrays;
import java.util.List;

/**
 * 任务链中参数不匹配
 * @author cocofhu
 */
public class CExecParamNotFoundException extends CJobException {
    public CExecParamNotFoundException(String msg) {
        super(msg);
    }

    public CExecParamNotFoundException(CPair<String,Class<?>> required, List<CExecParam> candidates) {
        super("parameter not match in this job, " +
                "require ( type: " + required.getSecond() + ", name: " + required.getFirst() + "), " +
                "but found(s) " + Arrays.toString(candidates.stream().map(param -> "( type: " + param.getType() + ", name: " + param.getName() + ")").toArray(String[]::new)));
    }
}
