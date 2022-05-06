package com.cocofhu.ctb.kernel;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.basic.CDebugExecutor;
import com.cocofhu.ctb.basic.CParamExecutor;
import com.cocofhu.ctb.basic.CUtilExecutor;
import com.cocofhu.ctb.basic.entity.JobDetail;
import com.cocofhu.ctb.basic.entity.JobParam;
import com.cocofhu.ctb.basic.CJobExecutor;
import com.cocofhu.ctb.kernel.core.config.CAbstractDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CTBPair;
import com.cocofhu.ctb.kernel.core.factory.CMethodBeanFactory;
import com.cocofhu.ctb.kernel.core.exec.*;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Startup {


    public static void main(String[] args) throws Exception {
        CMethodBeanFactory factory = new CMethodBeanFactory(() -> {
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

            result.add(new CAbstractDefinition(CJobExecutor.class) {
                @Override
                public String getBeanName() {
                    return "CJobExecutor";
                }
            });

            return result;
        });


        CJobExecutor cParse = new CJobExecutor();

        String test1 = new CUtilExecutor().readText("C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\json.json");
        String readTextText = new CUtilExecutor().readText("C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\exec\\读取文本.json");
        String toJobText = new CUtilExecutor().readText("C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\exec\\将JSON字符串转换成任务对象.json");
        String toExecutorText = new CUtilExecutor().readText("C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\exec\\任务对象转换为执行器.json");

        JobDetail readText = new CJobExecutor().readJobFromJson(readTextText);
        JobDetail toJob = new CJobExecutor().readJobFromJson(toJobText);
        JobDetail toExecutor = new CJobExecutor().readJobFromJson(toExecutorText);
        JobDetail test = new CJobExecutor().readJobFromJson(test1);

        JobDetail transform1 = new JobDetail("transform","transform",new JobParam[]{
                new JobParam("source","source",null,false,String.class)
        },new JobParam[]{
                new JobParam("dist","dist",null,false,String.class)
        },new CExecutorMethod("CParamExecutor",null,"transform", null)
                ,new CTBPair<>("source",CExecutor.EXEC_RETURN_VAL_KEY)
                ,new CTBPair<>("dist","json"));

        JobDetail transform2 = new JobDetail("transform","transform",new JobParam[]{
                new JobParam("source","source",null,false,String.class)
        },new JobParam[]{
                new JobParam("dist","dist",null,false,String.class)
        },new CExecutorMethod("CParamExecutor",null,"transform", null)
                ,new CTBPair<>("source",CExecutor.EXEC_RETURN_VAL_KEY)
                ,new CTBPair<>("dist","job"));


        JobDetail jobDetail = new JobDetail("SS","info",readText.getInputs(),toExecutor.getOutputs(),new JobDetail[]{
           readText,transform1, toJob,transform2,toExecutor
        });

        System.out.println("========================================================================");
        HashMap<String,Object> hashMap = new HashMap();
        hashMap.put("source","C:\\Users\\cocofhu\\IdeaProjects\\CTBKernel\\exec\\ReadFile.json");
        CExecutor executor = new CJobExecutor().forceRun(factory, jobDetail, hashMap);


        System.out.println(executor.getReturnVal());

    }

}
