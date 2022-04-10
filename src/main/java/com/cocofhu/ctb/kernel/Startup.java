package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.core.factory.CDefaultBeanFactory;

public class Startup{

    public String f(boolean x,int y){
        System.out.println(y);
        return x + "" + y;
    }

    public Startup(){
        System.out.println("===");
    }




    public static void main(String[] args) throws Exception {
        CDefaultBeanFactory factory = new CDefaultBeanFactory();
        System.out.println(factory.getBean("BCD"));
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
