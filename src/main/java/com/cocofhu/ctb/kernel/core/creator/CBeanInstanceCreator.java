package com.cocofhu.ctb.kernel.core.creator;

import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CConfig;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.exception.CBeanException;
import com.cocofhu.ctb.kernel.exception.bean.CInstantiationException;
import com.cocofhu.ctb.kernel.exception.bean.CNoConstructorResolverException;
import com.cocofhu.ctb.kernel.exception.bean.CNoConstructorException;


/**
 * Bean实例创建器
 * @author cocofhu
 */
public interface CBeanInstanceCreator extends CConstructorResolver{

    /**
     * 根据BeanDefinition实例化
     * @return 通过指定构造器创建的对象
     * @throws CNoConstructorResolverException  在调用此方法之前，没有设置任何一个ConstructorResolver
     * @throws CNoConstructorException          没有找到相应的构造方法
     * @throws CInstantiationException          创建Bean实例时发生了错误
     */
    Object newInstance(CBeanDefinition beanDefinition, CConfig config, CReadOnlyDataSet<String, Object> dataSet)
            throws CBeanException;

    /**
     * 注册一个CConstructorResolver，用于决定构造函数
     */
    void registerConstructorResolvers(CConstructorResolver resolver);


}
