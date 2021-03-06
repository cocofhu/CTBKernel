package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CSimpleExecutor;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.exception.exec.CExecParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.exec.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import com.cocofhu.ctb.kernel.util.ds.CWritableData;

import java.util.*;


public class CSimpleExecutorBuilder implements CExecutorBuilder {

    protected final CConfig config;


    public CSimpleExecutorBuilder(CConfig config) {
        this.config = config;

    }


    @Override
    public CExecutor toExecutor(CExecutorDefinition execDetail, CExecutorBuilder builder,
                                CWritableData<String, Class<?>> contextTypes,
                                CWritableData<String, Object> attachedValues,
                                String layer, boolean checkInput) {

        CExecutorUtils.checkParamValidAndThrow(execDetail.getInputs(), "inputs", layer);
        CExecutorUtils.checkParamValidAndThrow(execDetail.getOutputs(), "outputs", layer);
        CExecutorUtils.checkParamValidAndThrow(execDetail.getRemovals(), "removal", layer);

        // valRef typeRef只在当前方法里有效
        CWritableData<String, Object> valRef = new CDefaultWritableData<>(attachedValues);
        CWritableData<String, Class<?>> typeRef = new CDefaultWritableData<>(contextTypes);
        // 加入执行器自己的attachment
        if (execDetail.getAttachment() != null) {
            valRef.putAll(execDetail.getAttachment());
        }
        // 每个attachment都有自己的type
        valRef.entries().forEach(e -> {
            if (e.getKey() != null && e.getValue() != null) {
                typeRef.put(e.getKey(), e.getValue().getClass());
            }
        });


        // 解析输入参数
        CParameterDefinition[] inputs = execDetail.getInputs();
        List<CParameterDefinition> exactlyInputs = new ArrayList<>();
        if(inputs != null){
            for (CParameterDefinition input : inputs) {
                // 解析出正在的变量名称和类型
                CPair<String, Class<?>> parameter = resolveParameter(input, valRef, typeRef, layer);
                CPair<Boolean, List<CParameterDefinition>> checked = hasParam(typeRef, parameter);
                if (checkInput && !checked.getFirst()) {
                    throw new CExecParamNotFoundException(parameter, checked.getSecond(), layer);
                }
                // 如果输入参数不存在 则这个参数为真正需要的参数
                if(!checked.getFirst()){
                    exactlyInputs.add(input);
                }
                input.setType(parameter.getSecond());
                input.setName(parameter.getFirst());
            }
        }
        // 设置真正的需要的Input参数
        execDetail.setInputs(exactlyInputs.toArray(new CParameterDefinition[0]));

        // 解析输出参数, 这里需要合并这一次和上一次的输出
        // 因为在Removal中可能会引用这一次和上一次的输出
        CParameterDefinition[] outputs = execDetail.getOutputs();
        if(outputs != null){
            for (CParameterDefinition output : outputs) {
                CPair<String, Class<?>> parameter = resolveParameter(output, valRef, typeRef, layer);
                if (parameter.getFirst() != null && parameter.getSecond() != null) {
                    // 两个Map中都要加入对应的类型
                    contextTypes.put(parameter.getFirst(), parameter.getSecond());
                    typeRef.put(parameter.getFirst(), parameter.getSecond());
                }
                output.setType(parameter.getSecond());
                output.setName(parameter.getFirst());
            }
        }


        CParameterDefinition[] removals = execDetail.getRemovals();
        if(removals != null){
            for (CParameterDefinition removal : removals) {
                CPair<String, Class<?>> parameter = resolveParameter(removal, valRef, typeRef, layer);
                CPair<Boolean, List<CParameterDefinition>> checked = hasParam(typeRef, parameter);
                if (!checked.getFirst()) {
                    throw new CExecParamNotFoundException(parameter, checked.getSecond(), layer);
                }
                if (parameter.getFirst() != null && parameter.getSecond() != null) {
                    // 两个Map中都要去除
                    contextTypes.remove(parameter.getFirst());
                    typeRef.remove(parameter.getFirst());
                }
                removal.setType(parameter.getSecond());
                removal.setName(parameter.getFirst());
            }
        }


        CExecutorUtils.checkParamValidAndThrow(execDetail.getInputs(), "processed inputs", layer);
        CExecutorUtils.checkParamValidAndThrow(execDetail.getOutputs(), "processed outputs", layer);
        CExecutorUtils.checkParamValidAndThrow(execDetail.getRemovals(), "processed removal", layer);

        return new CSimpleExecutor(execDetail, config);
    }


    private CPair<String, Integer> parseReference(String str) {
        if (str == null) {
            return new CPair<>(null, 0);
        }
        int num = 0;
        while (num < str.length() && str.charAt(num) == '*') ++num;
        return new CPair<>(str.substring(num), num);
    }

    private CPair<String, Class<?>> resolveParameter(CParameterDefinition input, CWritableData<String, Object> valRef, CWritableData<String, Class<?>> typeRef, String layer) {
        CPair<String, Integer> nameRefPair = parseReference(input.getName());
        CPair<String, Integer> typeRefPair = null;
        Class<?> exactlyType = null;
        // dereference 找到真正的参数名称
        String exactlyName = dereferenceOfName(valRef, nameRefPair, layer);

        if (input.getType() instanceof String) {
            typeRefPair = parseReference((String) input.getType());
        } else if (input.getType() instanceof Class<?>) {
            exactlyType = (Class<?>) input.getType();
        } else {
            throw new CExecUnsupportedOperationException("can not parse type of input at layer "+ layer +", " + input.getType());
        }
        // dereference 找到真正的参数类型
        if (typeRefPair != null) {
            String name = dereferenceOfName(valRef, typeRefPair, layer);
            exactlyType = typeRef.get(name);
        }
        if (exactlyName == null || exactlyType == null) {
            throw new CExecParamNotFoundException("cannot resolve parameter of  " + input + " type or name is null at layer  "+ layer +":" +
                    "( type: " + exactlyType + ", name: " + exactlyName + "). ");
        }
        return new CPair<>(exactlyName, exactlyType);
    }

    private String dereferenceOfName(CWritableData<String, Object> ref, CPair<String, Integer> pair, String layer) {
        int times = pair.getSecond();
        String name = pair.getFirst();
        while (times-- > 0) {
            Object obj = ref.get(name);
            if (obj == null) {
                throw new CExecParamNotFoundException("parameter not found , dereference name of: " + name + " is null at layer "+ layer +". ");
            } else if (obj instanceof String) {
                name = (String) obj;
            } else {
                throw new CExecUnsupportedOperationException("can not dereference of " + pair + " at layer "+ layer +" from type which is not instanceof string, " + obj);
            }
        }
        return name;
    }

    private CPair<Boolean, List<CParameterDefinition>> hasParam(CWritableData<String, Class<?>> typeRef, CPair<String, Class<?>> pair) {
        List<CParameterDefinition> candidates = new ArrayList<>();
        if (typeRef == null) {
            return new CPair<>(false, candidates);
        }
        Map<String, Class<?>> map = typeRef.toReadOnlyMap();

        Set<String> keys = map.keySet();
        for(String key: keys){
            Class<?> type = map.get(key);
            if (pair.getSecond().isAssignableFrom(type)
                    && pair.getFirst().equals(key)) {
                candidates.clear();
                candidates.add(new CParameterDefinition(key,"",false,type));
                return new CPair<>(true, candidates);
            }
            if (pair.getSecond().isAssignableFrom(type)
                    || pair.getFirst().equals(key)) {
                candidates.add(new CParameterDefinition(key,"",false,type));
            }
        }

        return new CPair<>(false, candidates);
    }
}
