package com.cocofhu.ctb.kernel.core.exec.entity;

import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

public class CExecSummary {
    private final List<Map<String, Class<?>>> contextTypes;
    private final CExecDetail jobDetail;

    public CExecSummary(List<Map<String, Class<?>>> contextTypes, CExecDetail jobDetail) {
        this.contextTypes = contextTypes;
        this.jobDetail = jobDetail;
    }

    public List<Map<String, Class<?>>> getContextTypes() {
        return contextTypes;
    }

    public CExecDetail getJobDetail() {
        return jobDetail;
    }

    @Override
    public String toString() {

        StringBuilder sb = new StringBuilder();
        sb.append("Job Name: ").append(jobDetail.getName()).append(", Job Group: ").append(jobDetail.getGroup()).append("\n");
        sb.append("Job Info: ").append(jobDetail.getInfo()).append("\n");
        if (jobDetail.getType() == CExecDetail.TYPE_EXEC) {
            outSingleJob(jobDetail, sb);
        } else if (jobDetail.getType() == CExecDetail.TYPE_SCHEDULE) {
            CExecDetail[] subJobs = jobDetail.getSubJobs();
            for (int i = 0; i < subJobs.length; ++i) {
                CExecDetail subJob = subJobs[i];
                sb.append("Layer ").append(i).append("\n");
                outSingleJob(subJob, sb);
                sb.append("Context:\n");
                outContext(contextTypes.get(i), sb);
            }
        }
        return sb.toString();
    }

    private void outParams(CExecParam[] params, StringBuilder sb) {
        for (int i = 0; i < params.length; i++) {
            CExecParam p = params[i];
            sb.append("\t").append(i + 1).append(". (Name: ").append(p.getName()).append(", Type: ").append(p.getType()).append(")\n");
        }
    }

    private void outSingleJob(CExecDetail jobDetail, StringBuilder sb) {
        sb.append("Input:\n");
        outParams(jobDetail.getInputs(), sb);
        sb.append("Output:\n");
        outParams(jobDetail.getOutputs(), sb);
        sb.append("Removals:\n");
        outParams(jobDetail.getRemovals(), sb);
    }

    private void outContext(Map<String, Class<?>> context, StringBuilder sb) {
        AtomicInteger i = new AtomicInteger(1);
        context.forEach((s, c) -> sb.append("\t").append(i.getAndIncrement()).append(". (Name: ").append(s).append(", Type: ").append(c).append(")\n"));
    }
}
