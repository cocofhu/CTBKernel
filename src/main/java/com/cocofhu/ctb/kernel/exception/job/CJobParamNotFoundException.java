package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.core.exec.entity.CJobParam;
import com.cocofhu.ctb.kernel.core.config.CPair;
import com.cocofhu.ctb.kernel.exception.CBeanException;

import java.util.Arrays;
import java.util.List;

/**
 * 任务链中参数不匹配
 * @author cocofhu
 */
public class CJobParamNotFoundException extends CBeanException {
    public CJobParamNotFoundException(String msg) {
        super(msg);
    }

    public CJobParamNotFoundException(CPair<String,Class<?>> required, List<CJobParam> candidates) {
        super("parameter not match in this job, " +
                "require ( type: " + required.getSecond() + ", name: " + required.getFirst() + "), " +
                "but found(s) " + Arrays.toString(candidates.stream().map(param -> "( type: " + param.getType() + ", name: " + param.getName() + ")").toArray(String[]::new)));
    }
}
