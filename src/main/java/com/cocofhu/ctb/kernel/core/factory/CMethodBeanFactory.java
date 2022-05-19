package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.anno.param.process.CAutoWiredProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CExecutorInputProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CValueProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CBeanRefProcess;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.exec.compiler.CExecutorCompiler;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.*;
import com.cocofhu.ctb.kernel.util.CCollections;
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
    }

    @Override
    public CExecutorDefinition compiler(String expression) {

        // ctb.basic.test.mul > ctb.basic.test
        // a > b > c
        // a{} | b{} | c{}
        // factor = {|,>}
        // expression = string factor expression
        return null;
    }

    @Override
    public CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias) {
        return null;
    }
}
