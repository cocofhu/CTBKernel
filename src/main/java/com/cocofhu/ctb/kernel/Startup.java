package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.basic.CDebugExecutor;
import com.cocofhu.ctb.basic.CParamExecutor;
import com.cocofhu.ctb.basic.CUtilExecutor;
import com.cocofhu.ctb.kernel.core.exec.build.CDefaultExecutorBuilder;
import com.cocofhu.ctb.kernel.core.exec.build.CExecutorBuilder;
import com.cocofhu.ctb.kernel.core.exec.build.CExecutorUtils;
import com.cocofhu.ctb.kernel.core.exec.compiler.CExecutorCompiler;
import com.cocofhu.ctb.kernel.util.ds.CDefaultDefaultWritableDataSet;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.exec.entity.CParameterDefinition;
import com.cocofhu.ctb.kernel.core.config.CAbstractDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.exec.*;
import com.cocofhu.ctb.kernel.test.Power;
import org.apache.commons.cli.CommandLine;
import org.apache.commons.cli.CommandLineParser;
import org.apache.commons.cli.DefaultParser;
import org.apache.commons.cli.Options;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Startup implements CExecutorCompiler {


    public static void main(String[] args) throws Exception {
        CMethodBeanFactory factory = new CMethodBeanFactory((config) -> {
            List<CBeanDefinition> result = new ArrayList<>();
            result.add(new CAbstractDefinition(CParamExecutor.class) {
                @Override
                public String getBeanName() {
                    return "CParamExecutor";
                }
            });

            result.add(new CAbstractDefinition(CDebugExecutor.class) {
                @Override
                public String getBeanName() {
                    return "CDebugExecutor";
                }
            });

            result.add(new CAbstractDefinition(CUtilExecutor.class) {
                @Override
                public String getBeanName() {
                    return "CUtilExecutor";
                }
            });

            result.add(new CAbstractDefinition(CExecutorBuilder.class) {
                @Override
                public String getBeanName() {
                    return "CJobExecutor";
                }
            });

            result.add(new CAbstractDefinition(Power.class) {
                @Override
                public String getBeanName() {
                    return "Power";
                }
            });
            return result;
        });

        String group = "ctb.basic.param";
        // 由于任务的输入输出参数的类型和名字可能会动态确定，这里引入“引用”的概念
        // 1、关于输入输出参数的名字
        //      一个输入输出参数的名字由解引用符号+参数名字构成(这里称为引用标记)，例如*xyz，代表xyz变量所指向的变量
        // 2、关于输入输出参数的类型
        //      一个输入输出参数的类型有两种实现方式，若使用其他方式，则会抛出CUnsupportedOperationException异常：
        //          1）直接使用Class对象定义类型，例如String.class，代表该类型为String类型
        //          2）使用字符串间接定义类型引用标记，例如*xyz，代码使用解引用*xyz所指向的类型
        // 3、解引用
        //      我们已经了解到引用标记由两个部分构成，引用符号+参数名字构成，不妨定义引用符号*的数量为n,参数名字为s
        //  则解应用的过程为：
        //      while(n != 0) s = get(s)
        //  其中get(s)为从当前作用域中寻找s所指向变量的值，重复完成n次操作后s则为最终解除引用的值。
        // 4、当前作用域所包含的集合(含先后顺序)
        //  1）只允许Type引用：当前的输入输出(CJobParam)
        //  2) 允许Type和Name引用：用户自定义的attachment(函数柯里化参数)
        //  3) 只允许Type引用：Context对象
        //


//        CExecDetail job0 = new CExecDetail("X", "Y", group, new CExecParam[]{
//                new CExecParam("source", "source", false, String.class),
//        }, new CExecParam[]{
//                // 这里的type引用input里的type
//                new CExecParam("source", "source", false, "source"),
////                new CExecParam(CExecutor.EXEC_RETURN_VAL_KEY, "text", false, ArrayList.class)
//        },new CExecParam[]{
//                new CExecParam("source", "source", false, "source"),
////                new CJobParam(CExecutor.EXEC_RETURN_VAL_KEY, "text", false, ArrayList.class)
//        }, new CExecutorMethod("CUtilExecutor", null, "readText", null), null);
////
////
////        // 定义JOB1
////        // 输入参数为    (Name: source,              Type: class java.lang.String)
//        CExecDetail job1 = new CExecDetail("ParamTransformer", "ParamTransformer", group, new CExecParam[]{
//                // 这里的name引用attachment里source的值，type引用attachment里source的的类型
//                new CExecParam("*source", "source", false, CExecutor.EXEC_RETURN_VAL_KEY),
//        }, new CExecParam[]{
//                // 这里的name引用attachment里dist的值，type引用attachment里source的的类型
//                new CExecParam("*dist", "source", false, "*source")
//        },new CExecParam[]{
//                new CExecParam(CExecutor.EXEC_RETURN_VAL_KEY, "source", false, CExecutor.EXEC_RETURN_VAL_KEY),
////                new CJobParam("ABC", "source", false, "ABC"),
//                new CExecParam("*dist", "source", false, "*dist"),
//        }, new CExecutorMethod("CParamExecutor", null, "transform", null), null);
//
//        CDefaultDefaultWritableDataSet<String,Object> attachment = new CDefaultDefaultWritableDataSet<>();
//        attachment.put("source",CExecutor.EXEC_RETURN_VAL_KEY);
//        attachment.put("dist","ABC");
//        job1.setAttachment(attachment);
////
//        CExecDetail jobs = new CExecDetail("SimpleJob","a simple job",group,new CExecDetail[]{job0,job1},null);
//
//        CDefaultExecutorBuilder builder = new CDefaultExecutorBuilder(factory.getConfig());
//        System.out.println(builder.toExecutor(jobs, builder, new CExecutorContext()));
//        System.out.println(jobs);
//        CExecutor executor = new CJobExecutor().toExecutor(factory, jobs);
//        System.out.println(pair.getSecond());
//        new CJobExecutor().toExecutor(factory, pair.getSecond().getJobDetail());
//        System.out.println(f("C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\src"));
//
//        CExecDetail job = new CExecBuilder().toJobDetail(factory, new CExecutorMethod("Power", null, "mul", null));
//
//        System.out.println(new CExecBuilder().toSummary(factory,job));
//        CDefaultDefaultWritableDataSet<String,Object> attachment = new CDefaultDefaultWritableDataSet<>();
//        attachment.put("x",100);
//        attachment.put("y","99");
//
//
//        CExecutor executor = new CExecBuilder().forceRun(factory, job, attachment);
//        System.out.println(executor.getReturnVal());

//        System.out.println(JSON.toJSON(jobs));

        CExecutorDefinition job0 = CExecutorUtils.toExecDetail(factory,new CExecutorMethod("Power",null,"mul", null));
        CDefaultDefaultWritableDataSet<String,Object> attachment1 = new CDefaultDefaultWritableDataSet<>();
        attachment1.put("x",100);
        attachment1.put("y",2);
        job0.setAttachment(attachment1);
        CExecutorDefinition job1 = CExecutorUtils.toExecDetail(factory,new CExecutorMethod("CParamExecutor",null,"transform", null));

        CDefaultDefaultWritableDataSet<String,Object> attachment = new CDefaultDefaultWritableDataSet<>();
        attachment.put("source",CExecutionRuntime.EXEC_RETURN_VAL_KEY);
        attachment.put("dist","ABC");
        job1.setAttachment(attachment);

        CExecutorBuilder builder = new CDefaultExecutorBuilder(factory.getConfig());
        CExecutorDefinition[] subJobs = {job0, job1};
        CExecutorDefinition jobs = new CExecutorDefinition("SimpleJob","a simple job",group, subJobs,null);
        CExecutionRuntime context = new CExecutionRuntime();
        CExecutor executor = builder.toExecutor(jobs, builder, context);
        System.out.println(jobs);
        executor.run();
        System.out.println("你好");

        System.out.println(context);
        System.out.println(new Startup().compiler(" asd "));


        long l1 = System.currentTimeMillis();
        System.out.println(new Power().mul(100, 100));
        long l2 = System.currentTimeMillis();
        System.out.println(l2 - l1);


//        DefaultParser parser = new DefaultParser();
//        CommandLine cmd = parser.parse(new Options().addOption("e","ese"),new String[]{"-e","123"});
//        System.out.println(cmd.getOptionValue("e"));

    }

    public static void outParams(CParameterDefinition[] params){
        for (CParameterDefinition p: params
             ) {
            System.out.println("(Name: " + p.getName() + ", Type: " + p.getType() + ")" );
        }
    }

//    public static CTBPair<String,Integer> parseParam(String str){
//        if(str == null){
//            return new CTBPair<>(null,0);
//        }
//        int num = 0 ;
//        while(num < str.length() && str.charAt(num) == '*') ++num;
//        return new CTBPair<>(str.substring(num),num);
//    }

    public static void getArgumentsDetail(CExecutorDefinition job){





    }

    public static long f(String x) throws Exception {
        File file = new File(x);
//        System.out.println(x);
        BasicFileAttributes basicFileAttributes = Files.readAttributes(file.toPath(), BasicFileAttributes.class);
        if(basicFileAttributes.isRegularFile()){
            try (
                    BufferedReader bufferedReader = new BufferedReader(new FileReader(file))
            ){
                return bufferedReader.lines().count();
            }
        }else if(basicFileAttributes.isDirectory()){
            File[] files = file.listFiles();
            long ret = 0;
            assert files != null;
            for (File value : files) {
                ret += f(value.getAbsolutePath());
            }
            return ret;
        }
        return 0L;
    }

    @Override
    public CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias) {
        return null;
    }
}
