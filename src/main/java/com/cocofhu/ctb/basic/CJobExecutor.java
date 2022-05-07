package com.cocofhu.ctb.basic;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.basic.entity.JobDetail;
import com.cocofhu.ctb.basic.entity.JobParam;
import com.cocofhu.ctb.kernel.anno.CAttachmentArgs;
import com.cocofhu.ctb.kernel.anno.CAutowired;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.exec.CExecutorJob;
import com.cocofhu.ctb.kernel.core.exec.CSimpleExecutor;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.exception.CJobParamNotMatchException;
import com.cocofhu.ctb.kernel.exception.CUnsupportedOperationException;

import java.util.Map;

public class CJobExecutor {

    private CExecutor build(CBeanFactory factory, JobDetail job, CExecutorContext context) {
        if (job.getType() == JobDetail.TYPE_EXEC) {
            return new CSimpleExecutor(context, factory.getContext(), job.getMethod(), job.isIgnoreException(), job.getAttachment());
        } else if (job.getType() == JobDetail.TYPE_SCHEDULE) {
            CExecutor[] executors = new CExecutor[job.getSubJobs().length];
            JobParam[] lastOutput = null;
            for (int i = 0; i < job.getSubJobs().length; i++) {
                JobDetail subJob = job.getSubJobs()[i];
                if (lastOutput != null) {
                    JobParam[] inputs = subJob.getInputs();
                    for (int j = 0; j < inputs.length; j++) {
                        JobParam input = inputs[i];
                        if (!hasParam(lastOutput, input)) {
                            throw new CJobParamNotMatchException("parameter not match in this job, " +
                                    "require ( type: " + input.getType() + ", name: " + input.getName() + "). ");
                        }
                    }
                }
                executors[i] = build(factory, subJob, context);
                lastOutput = subJob.getInputs();
            }
            return new CExecutorJob(context, factory.getContext(), job.isIgnoreException(), executors);
        }
        throw new CUnsupportedOperationException("unsupported job type: " + job.getType());
    }

    private boolean hasParam(JobParam[] params, JobParam param) {
        if (params == null) {
            return false;
        }
        for (JobParam p : params) {
            if (param.getType().isAssignableFrom(p.getType()) &&
                    param.getName().equals(p.getName())) {
                return true;
            }
        }
        return false;
    }


    public CExecutor forceRun(@CAutowired CBeanFactory factory,
                              @CAttachmentArgs JobDetail job,
                              @CAttachmentArgs Map<String, Object> input) {
        CExecutor executor = toExecutor(factory, job);
        executor.setAttachment(input);
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();
        return executor;
    }

    public CExecutor toExecutor(@CAutowired CBeanFactory factory,
                                @CAttachmentArgs JobDetail job) {
        return build(factory, job, new CExecutorContext());
    }

    public JobDetail readJobFromJson(@CAttachmentArgs String json) {
        return JSON.parseObject(json, JobDetail.class);
    }

}
