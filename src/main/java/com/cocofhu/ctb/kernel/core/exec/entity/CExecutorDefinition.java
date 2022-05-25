package com.cocofhu.ctb.kernel.core.exec.entity;

import com.cocofhu.ctb.kernel.util.ds.CDefaultReadOnlyData;
import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.util.CCloneable;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter
@Setter
@EqualsAndHashCode
@ToString
public class CExecutorDefinition implements CCloneable {

    // 任务类型：多个任务
    public static final int TYPE_SCHEDULE = 1;
    // 任务类型：单个任务
    public static final int TYPE_EXEC = 2;
    // 任务类型： 服务
    public static final int TYPE_SVC = 3;
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
    private CDefaultReadOnlyData<String, Object> attachment;

    // 多任务时的子任务
    private CExecutorDefinition[] subJobs;

    // 任务的输入参数
    private CParameterDefinition[] inputs;
    // 任务的输出参数
    private CParameterDefinition[] outputs;

    // 任务执行完毕后会清除的参数
    private CParameterDefinition[] removals;

    // 说明
    private String info;

    // 分组
    private String group;

    // 任务属性
    private CDefaultReadOnlyData<String, Object> attributes;


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
    public CExecutorDefinition(String name, String info, String group, CParameterDefinition[] inputs,
                               CParameterDefinition[] outputs, CParameterDefinition[] removals, boolean ignoreException, CExecutorMethod method, CDefaultReadOnlyData<String, Object> attributes, CDefaultReadOnlyData<String, Object> attachment) {
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
    public CExecutorDefinition(String name, String info, String group, CParameterDefinition[] inputs,
                               CParameterDefinition[] outputs, CExecutorMethod method, CDefaultReadOnlyData<String, Object> attributes, CDefaultReadOnlyData<String, Object> attachment) {
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
    public CExecutorDefinition(String name, String info, String group, CParameterDefinition[] inputs,
                               CParameterDefinition[] outputs, CParameterDefinition[] removals, CExecutorMethod method, CDefaultReadOnlyData<String, Object> attributes, CDefaultReadOnlyData<String, Object> attachment) {
        this(name, info, group, inputs, outputs, removals, false, method, attributes, attachment);
    }

    public CExecutorDefinition(String name, String info, String group, CParameterDefinition[] inputs,
                               CParameterDefinition[] outputs, CParameterDefinition[] removals, CExecutorMethod method, CDefaultReadOnlyData<String, Object> attributes) {
        this(name, info, group, inputs, outputs, removals, false, method, attributes, null);
    }


    public CExecutorDefinition(String name, String info, String group, CExecutorDefinition[] subJobs, CDefaultReadOnlyData<String, Object> attributes, CDefaultReadOnlyData<String, Object> attachment) {
        this.version = VERSION;
        this.type = TYPE_SCHEDULE;
        this.name = name;
        this.info = info;
        this.group = group;
        this.subJobs = subJobs;
        this.attributes = attributes;
        this.attachment = attachment;
    }

    public CExecutorDefinition(String name, String info, String group, CExecutorDefinition[] subJobs, CDefaultReadOnlyData<String, Object> attributes) {
        this(name, info, group, subJobs, attributes, null);
    }

    public CExecutorDefinition() {
        this.version = VERSION;
    }


    /**
     * 构建一个服务定义
     */
    public static CExecutorDefinition newServiceDefinition(String name, String info, String group, CExecutorDefinition[] subJobs,
                                                           CParameterDefinition[] outputs, CExecutorMethod method, CDefaultReadOnlyData<String, Object> attributes, CDefaultReadOnlyData<String, Object> attachment){
        CExecutorDefinition definition = new CExecutorDefinition();
        definition.setName(name);
        definition.setInfo(info);
        definition.setGroup(group);
        definition.setSubJobs(subJobs);
        definition.setOutputs(outputs);
        definition.setMethod(method);
        definition.setAttributes(attributes);
        definition.setAttachment(attachment);
        definition.setType(TYPE_SVC);
        return definition;
    }

}
