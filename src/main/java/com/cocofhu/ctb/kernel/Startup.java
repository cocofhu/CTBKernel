package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.anno.param.CValue;
import com.cocofhu.ctb.kernel.core.config.CAbstractDefinition;
import com.cocofhu.ctb.kernel.core.config.CDefinition;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;

import java.util.List;

public class Startup{

    public long f(@CValue("10000") String xxx, CBeanFactory factory) throws Exception {
        System.out.println(xxx);
        return 11;
    }





    public static void main(String[] args) throws Exception {
        CMethodBeanFactory factory = new CMethodBeanFactory(new CBeanDefinitionResolver() {
            @Override
            public List<CDefinition> resolveAll() {
                return singeValue(new CAbstractDefinition(Startup.class) {
                    @Override
                    public String getBeanName() {
                        return "abc";
                    }
                });
            }
        });



        CExecutorContext context = new CExecutorContext();
        context.put("x","C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\src");

        CExecutorBuilder executorBuilder = new CExecutorBuilder(context,factory.getContext());

        CExecutor executor = executorBuilder.newExecutor("abc","f");
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();


        if(executor.isExecutedSuccessfully()){
            System.out.println("ReturnVal: " + executor.getReturnVal());
        }
        if(executor.isExceptionInContext()){
            System.out.println("Exception: " + executor.getThrowable().toString());
        }

//        try {
//            throw new RuntimeException("ttt");
//        }finally {
//            System.out.println(111);
//        }


    }

}
