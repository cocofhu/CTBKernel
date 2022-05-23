package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.*;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.exception.bean.CNoBeanDefinitionException;
import com.cocofhu.ctb.kernel.exception.bean.CNoConfigException;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyData;

/**
 * 为指定的BeanDefinition寻找构造函数
 * 需要保证所有实现类的 resolveConstructor 所返回的非空 CConstructorWrapper 的数量和小于等于 1
 * @author cocofhu
 */
@FunctionalInterface
public interface CConstructorResolver extends Comparable<CConstructorResolver>{
    /**
     * 寻找失败将返回空，继续寻找，寻找成功返回后终止
     */
    CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CConfig config, CReadOnlyData<String, Object> data) throws CBeanException;

    /**
     * 优先级 优先级越高排在越前面
     */
    default int priority() {
        return 0;
    }

    // FINAL
    default int compareTo(CConstructorResolver o) {
        return o.priority() - this.priority();
    }

    default void checkEmpty(CBeanDefinition beanDefinition, CConfig config) throws CBeanException{
        if(beanDefinition == null){
            throw new CNoBeanDefinitionException();
        }
        if(config == null){
            throw new CNoConfigException();
        }
    }
}
