package com.cocofhu.ctb.kernel.core.exec.build;

import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.exec.CSimpleExecutor;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecDetail;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecParam;
import com.cocofhu.ctb.kernel.exception.job.CExecParamNotFoundException;
import com.cocofhu.ctb.kernel.exception.job.CExecUnsupportedOperationException;
import com.cocofhu.ctb.kernel.util.ds.CDefaultLayerDataSet;
import com.cocofhu.ctb.kernel.util.ds.CPair;

import java.util.*;


public class CExecExecutorBuilder implements CExecutorBuilder {

    protected final CConfig config;


    public CExecExecutorBuilder(CConfig config) {
        this.config = config;

    }


    @Override
    public CPair<CExecutor, CExecParam[]> toExecutor(CExecDetail execDetail, CExecutorBuilder builder, CExecutorContext context,
                                                     CDefaultLayerDataSet<String, Class<?>> contextTypes, CExecParam[] lastOutput, boolean checkInput) {


        // set current context
        CDefaultLayerDataSet<String, Object> valRef = new CDefaultLayerDataSet<>();
        CDefaultLayerDataSet<String, Class<?>> typeRef = contextTypes.newLayer();

        if (execDetail.getAttachment() != null) {
            valRef.putAll(execDetail.getAttachment());
        }

        valRef.entries().forEach(e -> {
            if (e.getKey() != null && e.getValue() != null) {
                typeRef.put(e.getKey(), e.getValue().getClass());
            }
        });

        // 解析输入参数
        CExecParam[] inputs = execDetail.getInputs();
        for (CExecParam input : inputs) {
            // 解析出正在的变量名称和类型
            CPair<String, Class<?>> parameter = resolveParameter(input, valRef, typeRef);
            typeRef.put(parameter.getFirst(), parameter.getSecond());
            if (parameter.getFirst() != null && parameter.getSecond() != null) {
                typeRef.put(parameter.getFirst(), parameter.getSecond());
            }
            CPair<Boolean, List<CExecParam>> checked = hasParam(lastOutput, parameter);
            if (checkInput && !checked.getFirst()) {
                throw new CExecParamNotFoundException(parameter, checked.getSecond());
            }
            input.setType(parameter.getSecond());
            input.setName(parameter.getFirst());
        }
        // 解析输出参数, 这里需要合并这一次和上一次的输出
        // 因为在Removal中可能会引用这一次和上一次的输出
        CExecParam[] outputs = execDetail.getOutputs();
        if (lastOutput == null) {
            lastOutput = new CExecParam[outputs.length];
        } else {
            lastOutput = Arrays.copyOf(lastOutput, lastOutput.length + outputs.length);
        }

        for (int j = 0; j < outputs.length; ++j) {
            CExecParam output = outputs[j];
            CPair<String, Class<?>> parameter = resolveParameter(output, valRef, typeRef);
            if (parameter.getFirst() != null && parameter.getSecond() != null) {
                contextTypes.put(parameter.getFirst(), parameter.getSecond());
                typeRef.put(parameter.getFirst(), parameter.getSecond());
            }
            lastOutput[j + lastOutput.length - outputs.length] = output;
            output.setType(parameter.getSecond());
            output.setName(parameter.getFirst());
        }
        List<CExecParam> newOutput = new LinkedList<>(Arrays.asList(outputs));
        CExecParam[] removals = execDetail.getRemovals();
        for (CExecParam removal : removals) {
            CPair<String, Class<?>> parameter = resolveParameter(removal, valRef, typeRef);

            CPair<Boolean, List<CExecParam>> checked = hasParam(lastOutput, parameter);
            if (!checked.getFirst()) {
                throw new CExecParamNotFoundException(parameter, checked.getSecond());
            }
            CPair<Boolean, List<CExecParam>> matchedOutput = hasParam(outputs, parameter);

            if (matchedOutput.getFirst()) {
                newOutput.remove(matchedOutput.getSecond().get(0));
            }

            if (parameter.getFirst() != null && parameter.getSecond() != null) {
                contextTypes.remove(parameter.getFirst());
                typeRef.remove(parameter.getFirst());
            }
            removal.setType(parameter.getSecond());
            removal.setName(parameter.getFirst());
        }

        return new CPair<>(new CSimpleExecutor(context, config, execDetail.getMethod(), execDetail.isIgnoreException(), execDetail.getAttachment()), newOutput.toArray(new CExecParam[0]));
    }


    private CPair<String, Integer> parseReference(String str) {
        if (str == null) {
            return new CPair<>(null, 0);
        }
        int num = 0;
        while (num < str.length() && str.charAt(num) == '*') ++num;
        return new CPair<>(str.substring(num), num);
    }

    private CPair<String, Class<?>> resolveParameter(CExecParam input, CDefaultLayerDataSet<String, Object> valRef, CDefaultLayerDataSet<String, Class<?>> typeRef) {
        CPair<String, Integer> nameRefPair = parseReference(input.getName());
        CPair<String, Integer> typeRefPair = null;
        Class<?> exactlyType = null;
        // dereference 找到真正的参数名称
        String exactlyName = dereferenceOfName(valRef, nameRefPair);

        if (input.getType() instanceof String) {
            typeRefPair = parseReference((String) input.getType());
        } else if (input.getType() instanceof Class<?>) {
            exactlyType = (Class<?>) input.getType();
        } else {
            throw new CExecUnsupportedOperationException("can not parse type of input, " + input.getType());
        }
        // dereference 找到真正的参数类型
        if (typeRefPair != null) {
            String name = dereferenceOfName(valRef, typeRefPair);
            exactlyType = typeRef.get(name);
        }
        if (exactlyName == null || exactlyType == null) {
            throw new CExecParamNotFoundException("cannot resolve parameter of  " + input + " type or name is null :" +
                    "( type: " + exactlyType + ", name: " + exactlyName + "). ");
        }
        return new CPair<>(exactlyName, exactlyType);
    }

    private String dereferenceOfName(CDefaultLayerDataSet<String, Object> ref, CPair<String, Integer> pair) {
        int times = pair.getSecond();
        String name = pair.getFirst();
        while (times-- > 0) {
            Object obj = ref.get(name);
            if (obj == null) {
                throw new CExecParamNotFoundException("parameter not found , dereference name of: " + name + " is null. ");
            } else if (obj instanceof String) {
                name = (String) obj;
            } else {
                throw new CExecUnsupportedOperationException("can not dereference of " + pair + " from type which is not instanceof string, " + obj);
            }
        }
        return name;
    }

    private CPair<Boolean, List<CExecParam>> hasParam(CExecParam[] params, CPair<String, Class<?>> pair) {

        List<CExecParam> candidates = new ArrayList<>();

        if (params == null) {
            return new CPair<>(false, candidates);
        }
        for (CExecParam param : params) {
            if (pair.getSecond().isAssignableFrom((Class<?>) param.getType())
                    && pair.getFirst().equals(param.getName())) {
                candidates.clear();
                candidates.add(param);
                return new CPair<>(true, candidates);
            }
            if (pair.getSecond().isAssignableFrom((Class<?>) param.getType())
                    || pair.getFirst().equals(param.getName())) {
                candidates.add(param);
            }
        }
        return new CPair<>(false, candidates);
    }
}
