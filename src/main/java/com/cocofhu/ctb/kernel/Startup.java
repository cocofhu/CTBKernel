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
        File file = new File(x);
        System.out.println(x);
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        if(basicFileAttributes.isRegularFile()){
//            try (
//                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
//            ){
//                return bufferedReader.lines().count();
//            }
            return 1;
        }else if(basicFileAttributes.isDirectory()){
            File[] files = file.listFiles();
            long ret = 0;
            for (int i = 0; i < files.length; i++) {
                ret += f(files[i].getAbsolutePath(),factory);
            }
            return ret;
        }
        return 0L;
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

        CExecutorBuilder executorBuilder = new CExecutorBuilder(context,factory);

        CExecutor executor = executorBuilder.newExecutor("abc","f");
        executor.setStatus(CExecutor.Status.Ready);
        executor.run();


        if(executor.isExecutedSuccessfully()){
            System.out.println("ReturnVal: " + executor.getReturnVal());
        }
        if(executor.isExceptionInContext()){
            System.out.println("Exception: " + executor.getThrowable().toString());
        }


    }

}
