package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.anno.CConstructor;
import com.cocofhu.ctb.kernel.core.factory.CDefaultBeanFactory;
import org.apache.commons.beanutils.converters.IntegerConverter;

import java.util.HashMap;
import java.util.Map;

public class Startup{

    public String f(boolean x,int y){
        System.out.println(y);
        return x + "" + y;
    }

    public Startup(Double x,Double y){
        System.out.println(x+y);
    }




    public static void main(String[] args) throws Exception {
        CDefaultBeanFactory factory = new CDefaultBeanFactory();
        System.out.println(factory.getBean(Math.class));
//        System.out.println(Startup.class.getConstructor(null));
//        Map<String,Object> params = new HashMap<>();
//        params.put("a","10");
//        params.put("b","2");
////        IntegerConverter
//        Executor executor = new Executor(Math.class,"pow",params);
//        System.out.println(executor.execute());
//        CBeanFactory beanFactory = new CBeanFactory();
//        beanFactory.registerBean("abc",Startup.class);
//        System.out.println(beanFactory.getBean("abc"));
//        System.out.println(beanFactory.getBean("abc"));
//        System.out.println(beanFactory.getBean("abc"));
//        System.out.println(executor);
    }
}
