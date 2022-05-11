package com.cocofhu.ctb.kernel.core.exec.entity;

import com.cocofhu.ctb.kernel.core.config.CDefaultDefaultReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.util.CCloneable;

import java.util.Arrays;
import java.util.Map;

public class CJobDetail implements CCloneable {

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
    private CDefaultDefaultReadOnlyDataSet<String, Object> attachment;

    // 多任务时的子任务
    private CJobDetail[] subJobs;

    // 任务的输入参数
    private CJobParam[] inputs;
    // 任务的输出参数
    private CJobParam[] outputs;

    // 任务执行完毕后会清除的参数
    private CJobParam[] removals;

    // 说明
    private String info;

    // 分组
    private String group;

    // 任务属性
    private CDefaultDefaultReadOnlyDataSet<String, Object> attributes;


    /**
     * 创建一个默认的单任务
     *
     * @param name            任务名称
     * @param info            任务说明
     * @param group           任务分组
     * @param inputs          输入参数
     * @param outputs         输出参数
     * @param ignoreException 是否忽略异常
     * @param method          任务实现方法
     * @param attributes      任务属性
     * @param attachment      任务附加参数
     */
    public CJobDetail(String name, String info, String group, CJobParam[] inputs,
                      CJobParam[] outputs, CJobParam[] removals, boolean ignoreException, CExecutorMethod method, CDefaultDefaultReadOnlyDataSet<String, Object> attributes, CDefaultDefaultReadOnlyDataSet<String, Object> attachment) {
        this.name = name;
        this.version = VERSION;
        this.type = TYPE_EXEC;
        this.method = method;
        this.attachment = attachment;
        this.inputs = inputs;
        this.outputs = outputs;
        this.ignoreException = ignoreException;
        this.info = info;
        this.group = group;
        this.attributes = attributes;
        this.removals = removals;
    }

    /**
     * 创建一个默认的单任务，不忽略异常, 不清除任何参数
     *
     * @param name       任务名称
     * @param info       任务说明
     * @param group      任务分组
     * @param inputs     输入参数
     * @param outputs    输出参数
     * @param method     任务实现方法
     * @param attributes 任务属性
     * @param attachment 任务附加参数
     */
    public CJobDetail(String name, String info, String group, CJobParam[] inputs,
                      CJobParam[] outputs, CExecutorMethod method, CDefaultDefaultReadOnlyDataSet<String, Object> attributes, CDefaultDefaultReadOnlyDataSet<String, Object> attachment) {
        this(name, info, group, inputs, outputs, null, false, method, attributes, attachment);
    }

    /**
     * 创建一个默认的单任务，不忽略异常
     *
     * @param name       任务名称
     * @param info       任务说明
     * @param group      任务分组
     * @param inputs     输入参数
     * @param outputs    输出参数
     * @param removals   参数清除
     * @param method     任务实现方法
     * @param attributes 任务属性
     * @param attachment 任务附加参数
     */
    public CJobDetail(String name, String info, String group, CJobParam[] inputs,
                      CJobParam[] outputs, CJobParam[] removals, CExecutorMethod method, CDefaultDefaultReadOnlyDataSet<String, Object> attributes, CDefaultDefaultReadOnlyDataSet<String, Object> attachment) {
        this(name, info, group, inputs, outputs, removals, false, method, attributes, attachment);
    }

    public CJobDetail(String name, String info, String group, CJobParam[] inputs,
                      CJobParam[] outputs, CJobParam[] removals, CExecutorMethod method, CDefaultDefaultReadOnlyDataSet<String, Object> attributes) {
        this(name, info, group, inputs, outputs, removals, false, method, attributes, null);
    }


    public CJobDetail(String name, String info, String group, CJobDetail[] subJobs, CDefaultDefaultReadOnlyDataSet<String, Object> attributes, CDefaultDefaultReadOnlyDataSet<String, Object> attachment) {
        this.version = VERSION;
        this.type = TYPE_SCHEDULE;
        this.name = name;
        this.info = info;
        this.group = group;
        this.subJobs = subJobs;
        this.attributes = attributes;
        this.attachment = attachment;
    }

    public CJobDetail(String name, String info, String group, CJobDetail[] subJobs, CDefaultDefaultReadOnlyDataSet<String, Object> attributes) {
        this(name, info, group, subJobs, attributes, null);
    }


    public CJobParam[] getRemovals() {
        return removals;
    }

    public void setRemovals(CJobParam[] removals) {
        this.removals = removals;
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

    public CJobDetail[] getSubJobs() {
        return subJobs;
    }

    public void setSubJobs(CJobDetail[] subJobs) {
        this.subJobs = subJobs;
    }

    public CJobParam[] getInputs() {
        return inputs;
    }

    public void setInputs(CJobParam[] inputs) {
        this.inputs = inputs;
    }

    public CJobParam[] getOutputs() {
        return outputs;
    }

    public void setOutputs(CJobParam[] outputs) {
        this.outputs = outputs;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public CJobDetail() {
    }

    public String getGroup() {
        return group;
    }

    public void setGroup(String group) {
        this.group = group;
    }

    public CDefaultDefaultReadOnlyDataSet<String, Object> getAttachment() {
        return attachment;
    }

    public void setAttachment(CDefaultDefaultReadOnlyDataSet<String, Object> attachment) {
        this.attachment = attachment;
    }

    public CDefaultDefaultReadOnlyDataSet<String, Object> getAttributes() {
        return attributes;
    }

    public void setAttributes(CDefaultDefaultReadOnlyDataSet<String, Object> attributes) {
        this.attributes = attributes;
    }

    @Override
    public String toString() {
        return "CJobDetail{" +
                "name='" + name + '\'' +
                ", ignoreException=" + ignoreException +
                ", version=" + version +
                ", type=" + type +
                ", method=" + method +
                ", attachment=" + attachment +
                ", subJobs=" + Arrays.toString(subJobs) +
                ", inputs=" + Arrays.toString(inputs) +
                ", outputs=" + Arrays.toString(outputs) +
                ", removals=" + Arrays.toString(removals) +
                ", info='" + info + '\'' +
                ", group='" + group + '\'' +
                ", attributes=" + attributes +
                '}';
    }
}
