package com.cocofhu.ctb.kernel.core.config;

import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.exception.bean.CNoConfigException;
import com.cocofhu.ctb.kernel.exception.bean.CNoParameterException;
import com.cocofhu.ctb.kernel.exception.bean.CNoValueException;

import java.lang.annotation.Annotation;

/**
 * @author cocofhu
 */
public class CValueWrapper implements CMateData {
    /**
     * 该值是由哪一个值处理器处理出来的
     */
    private final CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> valueProcess;
    /**
     * 第二个参数用于表示是否处理成功
     */
    private final CPair<Object, Boolean> value;
    /**
     * 上下文
     */
    private final CConfig config;

    private final CParameterWrapper parameterWrapper;

    public CValueWrapper(CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> valueProcess,
                         CPair<Object, Boolean> value, CConfig config, CParameterWrapper parameterWrapper) {


        if (value == null) {
            throw new CNoValueException(valueProcess);
        }
        if (config == null) {
            throw new CNoConfigException();
        }
        if (parameterWrapper == null) {
            throw new CNoParameterException("empty parameter on build a value wrapper, where do you find this value: + " + value + ".");
        }

        this.valueProcess = valueProcess;
        this.value = value;
        this.config = config;
        this.parameterWrapper = parameterWrapper;
    }


    public CProcess<CPair<CParameterWrapper, CReadOnlyDataSet<String, Object>>> getValueProcess() {
        return valueProcess;
    }

    public CPair<Object, Boolean> getValue() {
        return value;
    }

    @Override
    public String toString() {
        return "CValueWrapper{" +
                "valueProcess=" + valueProcess +
                ", value=" + value +
                ", config=" + config +
                ", parameterWrapper=" + parameterWrapper +
                '}';
    }

    @Override
    public Annotation[] getAnnotations() {
        return new Annotation[0];
    }

    @Override
    public <T extends Annotation> T getAnnotation(Class<T> clazz) {
        return null;
    }

    @Override
    public CMateData getParent() {
        return parameterWrapper;
    }
}
