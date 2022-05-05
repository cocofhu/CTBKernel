package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.basic.param.CParamExecutor;
import com.cocofhu.ctb.kernel.anno.param.CValue;
import com.cocofhu.ctb.kernel.core.config.CAbstractDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;

import java.util.List;

public class Startup{






    public static void main(String[] args) throws Exception {
        CMethodBeanFactory factory = new CMethodBeanFactory(new CBeanDefinitionResolver() {
            @Override
            public List<CBeanDefinition> resolveAll() {
                return singeValue(new CAbstractDefinition(CParamExecutor.class) {
                    @Override
                    public String getBeanName() {
                        return "abc";
                    }
                });
            }
        });



        CExecutorContext context = new CExecutorContext();
        context.put("inx","1000");
        context.put("#CParamExecutor_source","inx");
        context.put("#CParamExecutor_dist","out");

        CExecutorBuilder executorBuilder = new CExecutorBuilder(context,factory.getContext());

        CExecutor transform = executorBuilder.newExecutor("abc","transform");
        CExecutor removeKey = executorBuilder.newExecutor("abc","removeKey");
        CExecutor putKey = executorBuilder.newExecutor("abc","putKey");
        CExecutor output = executorBuilder.newExecutor("abc","output");




        transform.setStatus(CExecutor.Status.Ready);
        putKey.setStatus(CExecutor.Status.Ready);
        removeKey.setStatus(CExecutor.Status.Ready);
        output.setStatus(CExecutor.Status.Ready);

        transform.run();
        output.run();



//        try {
//            throw new RuntimeException("ttt");
//        }finally {
//            System.out.println(111);
//        }


    }

}
