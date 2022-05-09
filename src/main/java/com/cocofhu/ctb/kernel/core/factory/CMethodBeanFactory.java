package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.anno.param.process.CAutoWiredProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CExecutorInputProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CValueProcess;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.anno.param.process.CBeanRefProcess;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.*;

/**
 * @author cocofhu
 */
public class CMethodBeanFactory extends CDefaultBeanFactory {




    @SuppressWarnings("unchecked")
    public CMethodBeanFactory(CBeanDefinitionResolver beanDefinitionResolver) {
        super(new CDefaultBeanInstanceCreator(new CConstructorResolver[]{new CDefaultConstructorResolver(), new CDefaultNoParameterConstructorResolver()}),
                beanDefinitionResolver,
                new CChainValueResolver(
                        new CValueResolver[]{
                                new CAnnotationValueResolver(new CProcess[]{new CValueProcess(), new CBeanRefProcess(), new CAutoWiredProcess(), new CExecutorInputProcess()})
                        })
        );
    }






}
