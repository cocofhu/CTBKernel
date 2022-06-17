package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.anno.param.process.CAutoWiredProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CExecutorInputProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CValueProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CBeanRefProcess;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.core.exec.build.CExecutorUtils;
import com.cocofhu.ctb.kernel.core.exec.compiler.CExecutorCompiler;
import com.cocofhu.ctb.kernel.core.exec.compiler.CExecutorDefinitionResolver;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.*;
import com.cocofhu.ctb.kernel.util.CCollections;
import com.cocofhu.ctb.kernel.util.ds.CDefaultWritableData;
import com.cocofhu.ctb.kernel.util.ds.CPair;
import lombok.extern.slf4j.Slf4j;

import java.lang.reflect.Method;
import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author cocofhu
 */
@Slf4j
public class CMethodBeanFactory extends CDefaultBeanFactory implements CExecutorDefinitionResolver {

    private final Map<String, CExecutorDefinition> executorDefinitionMap = new HashMap<>(32);
    private final Map<String, String> alias = new HashMap<>();

    private void registerExec(String beanName){
        CBeanDefinition beanDefinition = getBeanDefinition(beanName);
        Method[] methods = beanDefinition.getBeanClass().getMethods();
        for (Method method : methods) {
            CExecutorDefinition exec = CExecutorUtils.toExecDetail(this, new CExecutorMethod(beanName, beanDefinition.getBeanClass(), method.getName(), method.getParameterTypes()));
            if(exec != null){
                executorDefinitionMap.put(exec.getName(), exec);
                log.info("register exec: "+ exec.getName() +", detail: " + exec);
            }
        }
    }
    
    public CMethodBeanFactory(CBeanDefinitionResolver beanDefinitionResolver) {
        super(new CDefaultBeanInstanceCreator(new CConstructorResolver[]{new CDefaultConstructorResolver(), new CDefaultNoParameterConstructorResolver()}),
                beanDefinitionResolver,
                new CChainValueResolver(
                        new CValueResolver[]{
                                new CAnnotationValueResolver(CCollections.toList(new CValueProcess(), new CBeanRefProcess(), new CAutoWiredProcess(), new CExecutorInputProcess()))
                        })
        );

        registerExec("CUtilExecutor");

    }


    @Override
    public CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias) {
        CExecutorDefinition definition = executorDefinitionMap.get(nameOrAlias);
        return definition == null ? null : (CExecutorDefinition) definition.cloneSelf();
    }

//    @Override
//    public CExecutorDefinition compiler(String expression) {
//        return CExecutorCompiler.super.compiler(expression);
//    }

}
