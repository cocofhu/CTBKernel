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

import java.util.*;
import java.util.function.BiConsumer;

/**
 * @author cocofhu
 */
public class CMethodBeanFactory extends CDefaultBeanFactory implements CExecutorDefinitionResolver {

    private final Map<String, CExecutorDefinition> executorDefinitionMap = new HashMap<>(32);
    private final Map<String, String> alias = new HashMap<>();

    public CMethodBeanFactory(CBeanDefinitionResolver beanDefinitionResolver) {
        super(new CDefaultBeanInstanceCreator(new CConstructorResolver[]{new CDefaultConstructorResolver(), new CDefaultNoParameterConstructorResolver()}),
                beanDefinitionResolver,
                new CChainValueResolver(
                        new CValueResolver[]{
                                new CAnnotationValueResolver(CCollections.toList(new CValueProcess(), new CBeanRefProcess(), new CAutoWiredProcess(), new CExecutorInputProcess()))
                        })
        );

        List<CBeanDefinition> beanDefinitions = beanDefinitionResolver.resolveAll(getConfig());


        CExecutorDefinition job0 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("Power", "mul"));
        CExecutorDefinition job1 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("CParamExecutor", "transform"));
        CExecutorDefinition job2 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("CDBUtils", "acquireConnection"));
        CExecutorDefinition job3 = CExecutorUtils.toExecDetail(this, new CExecutorMethod("CDBUtils", "queryAsMap"));

        CDefaultWritableData<String, Object> attachment = new CDefaultWritableData<>();
        attachment.put("source", CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY);
        attachment.put("dist", "ABC");
        job1.setAttachment(attachment);

        attachment = new CDefaultWritableData<>();
        attachment.put("driverName", "com.mysql.cj.jdbc.Driver");
        attachment.put("username", "root");
        job2.setAttachment(attachment);

        executorDefinitionMap.put("Power", job0);
        executorDefinitionMap.put("Transform", job1);
        executorDefinitionMap.put("AcquireConnection", job2);
        executorDefinitionMap.put("QueryAsMapList", job3);

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
