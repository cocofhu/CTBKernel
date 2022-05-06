package com.cocofhu.ctb.basic.entity;

import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

public class JobDetail {

    // 任务类型：多个任务
    public static final int TYPE_SCHEDULE = 1;
    // 任务类型：单个任务
    public static final int TYPE_EXEC = 2;
    // 版本号：0
    public static final int VERSION = 0;

    // 任务名称
    private String name;

    // 是否忽略上次异常，用与多个任务串行执行时，忽略或者处理上一个任务出现的异常
    private boolean ignoreException;

    // 版本号
    private int version;

    // 任务类型
    private int type;

    // 任务具体执行Method
    private CExecutorMethod method;

    // 任务的附加参数
    private Map<String, Object> attachment;

    // 多任务时的子任务
    private JobDetail[] subJobs;

    // 任务的输入参数
    private JobParam[] inputs;
    // 任务的输出参数
    private JobParam[] outputs;

    // 说明
    private String info;

    // 分组
    private String group;


    /**
     * 创建一个默认的单任务
     *
     * @param name            任务名称
     * @param info            任务说明
     * @param inputs          输入参数
     * @param outputs         输出参数
     * @param ignoreException 是否忽略异常
     * @param method          任务实现方法
     * @param attachment      任务附加参数
     */
    @SuppressWarnings("unchecked")
    public JobDetail(String name, String info, JobParam[] inputs,
                     JobParam[] outputs, boolean ignoreException, CExecutorMethod method, CTBPair<String, Object>... attachment) {
        this.name = name;
        this.version = VERSION;
        this.type = TYPE_EXEC;
        this.method = method;
        this.attachment = new HashMap<>();
        for (CTBPair<String, Object> pair : attachment) {
            this.attachment.put(pair.getFirst(), pair.getSecond());
        }
        this.inputs = inputs;
        this.outputs = outputs;
        this.ignoreException = ignoreException;
        this.info = info;
    }

    /**
     * 创建一个默认的单任务，不忽略异常
     *
     * @param name       任务名称
     * @param info       任务说明
     * @param inputs     输入参数
     * @param outputs    输出参数
     * @param method     任务实现方法
     * @param attachment 任务附加参数
     */
    @SuppressWarnings("unchecked")
    public JobDetail(String name, String info, JobParam[] inputs,
                     JobParam[] outputs, CExecutorMethod method, CTBPair<String, Object>... attachment) {
        this(name, info, inputs, outputs, false, method, attachment);
    }

    /**
     * 创建一个默认的单任务，不忽略异常，没有输入参数
     *
     * @param name       任务名称
     * @param info       任务说明
     * @param inputs     输入参数
     * @param method     任务实现方法
     * @param attachment 任务附加参数
     */
    @SuppressWarnings("unchecked")
    public JobDetail(String name, String info, JobParam[] inputs, CExecutorMethod method, CTBPair<String, Object>... attachment) {
        this(name, info, inputs, null, false, method, attachment);
    }


    /**
     * 创建多个任务(任务组)
     *
     * @param name            任务名称
     * @param info            任务说明
     * @param inputs          输入参数
     * @param outputs         输出参数
     * @param subJobs         子任务
     * @param ignoreException 是否忽略异常
     * @param attachment      任务附加参数，附加参数只会传到第一个子任务中
     */
    @SuppressWarnings("unchecked")
    public JobDetail(String name, String info, JobParam[] inputs,
                     JobParam[] outputs, JobDetail[] subJobs, boolean ignoreException, CTBPair<String, Object>... attachment) {
        this.name = name;
        this.subJobs = subJobs;
        this.type = TYPE_SCHEDULE;
        this.version = VERSION;
        this.inputs = inputs;
        this.outputs = outputs;
        this.ignoreException = ignoreException;
        this.attachment = new HashMap<>();
        for (CTBPair<String, Object> pair : attachment) {
            this.attachment.put(pair.getFirst(), pair.getSecond());
        }
        this.info = info;
    }

    /**
     * 创建多个任务(任务组),不忽略异常
     *
     * @param name       任务名称
     * @param info       任务说明
     * @param inputs     输入参数
     * @param outputs    输出参数
     * @param subJobs    子任务
     * @param attachment 任务附加参数，附加参数只会传到第一个子任务中
     */
    @SuppressWarnings("unchecked")
    public JobDetail(String name, String info, JobParam[] inputs,
                     JobParam[] outputs, JobDetail[] subJobs, CTBPair<String, Object>... attachment) {
        this(name, info, inputs, outputs, subJobs, false, attachment);
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public boolean isIgnoreException() {
        return ignoreException;
    }

    public void setIgnoreException(boolean ignoreException) {
        this.ignoreException = ignoreException;
    }

    public int getVersion() {
        return version;
    }

    public void setVersion(int version) {
        this.version = version;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public CExecutorMethod getMethod() {
        return method;
    }

    public void setMethod(CExecutorMethod method) {
        this.method = method;
    }

    public Map<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(Map<String, Object> attachment) {
        this.attachment = attachment;
    }

    public JobDetail[] getSubJobs() {
        return subJobs;
    }

    public void setSubJobs(JobDetail[] subJobs) {
        this.subJobs = subJobs;
    }

    public JobParam[] getInputs() {
        return inputs;
    }

    public void setInputs(JobParam[] inputs) {
        this.inputs = inputs;
    }

    public JobParam[] getOutputs() {
        return outputs;
    }

    public void setOutputs(JobParam[] outputs) {
        this.outputs = outputs;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public JobDetail() {
    }

    @Override
    public String toString() {
        return "JobDetail{" +
                "name='" + name + '\'' +
                ", ignoreException=" + ignoreException +
                ", version=" + version +
                ", type=" + type +
                ", method=" + method +
                ", attachment=" + attachment +
                ", subJobs=" + Arrays.toString(subJobs) +
                ", inputs=" + Arrays.toString(inputs) +
                ", outputs=" + Arrays.toString(outputs) +
                '}';
    }
}
