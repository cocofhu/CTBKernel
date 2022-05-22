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
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.*;
import com.cocofhu.ctb.kernel.util.CCollections;
import com.cocofhu.ctb.kernel.util.ds.CDefaultDefaultWritableDataSet;
import com.cocofhu.ctb.kernel.util.ds.CReadOnlyDataSet;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author cocofhu
 */
public class CMethodBeanFactory extends CDefaultBeanFactory implements CExecutorCompiler {

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


        CExecutorDefinition job0 = CExecutorUtils.toExecDetail(this,new CExecutorMethod("Power",null,"mul", null));
        CExecutorDefinition job1 = CExecutorUtils.toExecDetail(this,new CExecutorMethod("CParamExecutor",null,"transform", null));

        CDefaultDefaultWritableDataSet<String,Object> attachment = new CDefaultDefaultWritableDataSet<>();
        attachment.put("source", CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY);
        attachment.put("dist","ABC");
        job1.setAttachment(attachment);

        executorDefinitionMap.put("Power", job0);
        executorDefinitionMap.put("Transform", job1);

    }


    @Override
    public CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias) {
        return (CExecutorDefinition) executorDefinitionMap.get(nameOrAlias).cloneSelf();
    }
}
