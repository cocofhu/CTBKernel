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

}
