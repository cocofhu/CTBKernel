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
     * 注解迭代器，递归迭代遍历该元素上的所有指定注解
     *
     * @param <T> 注解类型
     */
    @FunctionalInterface
    interface CAnnotationIterator<T> {
        // 递归迭代遍历该元素上的所有指定注解, 返回false是将终止迭代
        boolean iterate(T anno);
    }

    /**
     * 获取该结构上的指定注解，如果寻找不到，则向父级寻找
     */
    default <T extends Annotation> T acquireNearAnnotation(Class<T> clazz) {
        return annotationForEachRecursively(clazz, anno -> false);
    }

    /**
     * 递归迭代遍历该元素上的所有指定注解
     *
     * @param clazz    注解类型
     * @param iterator 遍历器
     * @return 最后一次迭代的注解
     */
    default <T extends Annotation> T annotationForEachRecursively(Class<T> clazz, CAnnotationIterator<T> iterator) {
        CMateData current = this;
        T annotation = null;
        while (current != null) {
            annotation = current.getAnnotation(clazz);
            if (annotation != null) {
                if (!iterator.iterate(annotation)) {
                    return annotation;
                }
            }
            current = current.getParent();
        }
        return annotation;
    }


}
