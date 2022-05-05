package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.core.config.CAbstractBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.exec.*;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.exception.CBadBeanDefinitionException;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.List;

public class Startup{

    public long f(String x, CBeanFactory factory) throws Exception {
        System.out.println(x);
        return 11;
    }





    public static void main(String[] args) throws Exception {
        CMethodBeanFactory factory = new CMethodBeanFactory(new CBeanDefinitionResolver() {
            @Override
            public List<CBeanDefinition> resolveAll() {
                return singeValue(new CAbstractBeanDefinition(Startup.class) {
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

        try {
            throw new RuntimeException("ttt");
        }finally {
            System.out.println(111);
        }


    }

}
