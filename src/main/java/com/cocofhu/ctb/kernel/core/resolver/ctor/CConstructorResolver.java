package com.cocofhu.ctb.kernel.core.resolver.ctor;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.exception.CBadBeanDefinitionException;
import com.cocofhu.ctb.kernel.exception.CBadCTBContextException;

/**
 * 为指定的BeanDefinition寻找构造函数
 * 需要保证所有实现类的 resolveConstructor 所返回的非空 CConstructorWrapper 的数量和小于等于 1
 */
@FunctionalInterface
public interface CConstructorResolver extends Comparable<CConstructorResolver>{
    /**
     * 寻找失败将返回空，继续寻找，寻找成功返回后终止
     */
    CExecutableWrapper resolveConstructor(CBeanDefinition beanDefinition, CTBContext context);

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

    default void checkEmpty(CBeanDefinition beanDefinition, CTBContext context){
        if(beanDefinition == null){
            throw new CBadBeanDefinitionException("empty bean definition.");
        }
        if(context == null){
            throw new CBadCTBContextException("empty ctb context.");
        }
    }
}
