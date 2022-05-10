package com.cocofhu.ctb.kernel.core.config;

import java.lang.annotation.Annotation;

/**
 * 元信息类，用于递归向上查找
 * @author cocofhu
 */
public interface CMateData {
    /**
     * 获取该结构上的所有注解
     */
    Annotation[] getAnnotations();

    /**
     * 获取该结构上的指定注解
     */
    <T extends Annotation> T getAnnotation(Class<T> clazz) ;

    /**
     * 获得父级的原信息
     * 这里不要用默认实现，某些实现类可能忘记实现该接口，导致意外的BUG (●'◡'●)
     */
    /*default*/ CMateData getParent();

    /**
     * 获取该结构上的指定注解，如果寻找不到，则向父级寻找
     */
    default <T extends Annotation> T acquireNearAnnotation(Class<T> clazz){
        CMateData current = this;
        while(current != null){
            T annotation = current.getAnnotation(clazz);
            if(annotation != null){
                return annotation;
            }
            current = current.getParent();
        }
        return null;
    }
}
