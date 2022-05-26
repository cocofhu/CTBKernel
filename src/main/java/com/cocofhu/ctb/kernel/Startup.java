package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.basic.CDBUtils;
import com.cocofhu.ctb.basic.CDebugExecutor;
import com.cocofhu.ctb.basic.CParamExecutor;
import com.cocofhu.ctb.basic.CUtilExecutor;
import com.cocofhu.ctb.basic.grpc.CGRPCBasicExecutor;
import com.cocofhu.ctb.kernel.core.exec.CDefaultExecutionRuntime;
import com.cocofhu.ctb.kernel.core.exec.CExecutor;
import com.cocofhu.ctb.kernel.core.exec.build.CDefaultExecutorBuilder;
import com.cocofhu.ctb.kernel.core.exec.build.CExecutorBuilder;
import com.cocofhu.ctb.kernel.core.exec.compiler.CFMSExecutorCompiler;
import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.core.config.CAbstractDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.test.Power;
import com.cocofhu.ctb.kernel.util.ds.CLayerData;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileReader;
import java.nio.file.Files;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.*;

public class Startup {


    public static void main(String[] args) throws Exception {
        // CGRPCService -port 9090 > Transform -source grpcData -dist sql > ReadMySQLURLAndPassword -source "mysql.properties" > AcquireConnection > Transform -dist connection > QueryAsMapList > Debug
//        System.out.println(f("C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\src"));
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

            result.add(new CAbstractDefinition(CDBUtils.class) {
                @Override
                public String getBeanName() {
                    return "CDBUtils";
                }
            });

            result.add(new CAbstractDefinition(CDebugExecutor.class) {
                @Override
                public String getBeanName() {
                    return "CDebugExecutor";
                }
            });

            result.add(new CAbstractDefinition(CGRPCBasicExecutor.class) {
                @Override
                public String getBeanName() {
                    return "CGRPCBasicExecutor";
                }
            });

            return result;
        });

//        CLayerData<String, Object> layer0 = new CDefaultLayerData<>();
//        layer0.put("layer0","layer0");
//        layer0.put("data","layer0");
//        CLayerData<String, Object> layer1 = layer0.newLayer();
//        layer1.put("data","layer1");
//        layer1.put("layer1","layer1");
//        CLayerData<String, Object> layer2 = layer1.newLayer();
//        layer2.put("data","layer2");
//        layer2.put("layer2","layer2");
//        CLayerData<String, Object> layer3 = layer2.newLayer();
//        layer3.put("data","layer3");
//        layer3.put("layer3","layer3");
//        CLayerData<String, Object> layer4 = layer3.newLayer();
//        layer4.put("data","layer4");
//        layer4.put("layer4","layer4");
//        CLayerData<String, Object> layer5 = layer4.newLayer();
//        layer5.put("data","layer5");
//        layer5.put("layer5","layer5");
//
//        showLayer("layer0",layer0);
//        showLayer("layer1",layer1);
//        showLayer("layer2",layer2);
//        showLayer("layer3",layer3);
//        showLayer("layer4",layer4);
//        showLayer("layer5",layer5);

        testCompiler(factory);

    }


    private static void showLayer(String msg, CLayerData<String, Object> layer){
        int depth = layer.depth();
        System.out.println("##################################");
        System.out.println(msg);
        System.out.println("ALL MAP:\t" + layer.toReadOnlyMap());
        System.out.println("ALL ENTRIES:" +layer.entries());
        System.out.println("----------------------");
        for (int i = 0; i <= depth; i++) {
            System.out.println("Depth:" + i);
            System.out.println("ToMap:" + layer.toReadOnlyMap(i));
            System.out.println("Entries:" + layer.entries(i));
        }
        System.out.println("----------------------");
        System.out.println();

    }

    private static void testCompiler(CMethodBeanFactory factory){
        Scanner scan = new Scanner(System.in);
        String source = scan.nextLine();
        CExecutorBuilder builder = new CDefaultExecutorBuilder(factory.getConfig());
        CDefaultExecutionRuntime context = new CDefaultExecutionRuntime();
        CExecutorDefinition definition = new CFMSExecutorCompiler(factory).compiler(source, 0);
        System.out.println(definition);
        CExecutor executor = builder.toExecutor(definition, builder, true);
//        new CServiceExecutor(context,factory.getConfig(),)
        //CGRPCService -port 9090 > Transform -source grpcData -dist abc
        //CGRPCService -port 9090 > Debug

        executor.run(context);
        System.out.println(context);
        System.out.println(context.getReturnVal());
    }



    public static long f(String x) throws Exception {
        File file = new File(x);
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

}

//        while(CStringUtils.isEmpty(source)) source = scan.next();
//        String group = "ctb.basic.param";
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

//        CExecutorDefinition job0 = CExecutorUtils.toExecDetail(factory,new CExecutorMethod("Power",null,"mul", null));
//        CDefaultDefaultWritableDataSet<String,Object> attachment1 = new CDefaultDefaultWritableDataSet<>();
//        attachment1.put("x",100);
//        attachment1.put("y",2);
//        job0.setAttachment(attachment1);
//        CExecutorDefinition job1 = CExecutorUtils.toExecDetail(factory,new CExecutorMethod("CParamExecutor",null,"transform", null));
//
//        CDefaultDefaultWritableDataSet<String,Object> attachment = new CDefaultDefaultWritableDataSet<>();
//        attachment.put("source", CDefaultExecutionRuntime.EXEC_RETURN_VAL_KEY);
//        attachment.put("dist","ABC");
//        job1.setAttachment(attachment);
//
//        CExecutorBuilder builder = new CDefaultExecutorBuilder(factory.getConfig());
//        CExecutorDefinition[] subJobs = {job0, job1};
//        CExecutorDefinition jobs = new CExecutorDefinition("SimpleJob","a simple job",group, subJobs,null);
//        CDefaultExecutionRuntime context = new CDefaultExecutionRuntime();
//        CExecutor executor = builder.toExecutor(jobs, builder, context);
////        System.out.println(jobs);
//        executor.run();
//        System.out.println("返回值是：" + context.getReturnVal());
////        System.out.println("返回值是：" + context.getCurrentLayer().toMap());
//        System.out.println(context);

//        System.out.println(context);
//        System.out.println(new Startup().compiler(" asd "));
//
//
//        long l1 = System.currentTimeMillis();
//        System.out.println(new Power().mul(100, 100));
//        long l2 = System.currentTimeMillis();
//        System.out.println(l2 - l1);


//        DefaultParser parser = new DefaultParser();
//        CommandLine cmd = parser.parse(new Options().addOption("e","ese"),new String[]{"-e","123"});
//        System.out.println(cmd.getOptionValue("e"));
//        CExecutorBuilder builder = new CDefaultExecutorBuilder(factory.getConfig());
//        CDefaultExecutionRuntime context;
//        Scanner scan = new Scanner(System.in);
//        String source = scan.nextLine();
//        CExecutorDefinition definition = factory.compiler(source);
//        for (CExecutorDefinition d: definition.getSubJobs()
//             ) {
//            System.out.println(Arrays.toString(d.getInputs()));
//            System.out.println(Arrays.toString(d.getOutputs()));
//        }
//        builder.toExecutor(definition,builder,context = new CDefaultExecutionRuntime()).run();
//        System.out.println(context);
//        System.out.println(context.getReturnVal());
//        factory.f();

//        System.out.println(f("/Users/hufeng/IdeaProjects/CTBKernel/src"));
//        new CFMSExecutorCompiler().testParseToken("'\\\\ss' ");
