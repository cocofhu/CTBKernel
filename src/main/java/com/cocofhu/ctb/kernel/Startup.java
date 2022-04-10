package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.aware.CBeanNameAware;
import com.cocofhu.ctb.kernel.core.config.CAbstractBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.CDefaultBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;

import java.util.ArrayList;
import java.util.List;

public class Startup implements CBeanFactoryAware , CBeanNameAware {

    public String f(boolean x,int y){
        System.out.println(y);
        return x + "" + y;
    }

    public Startup(){
        System.out.println("===");
    }




    public static void main(String[] args) throws Exception {
        CDefaultBeanFactory factory = new CDefaultBeanFactory(new CDefaultBeanInstanceCreator(), () -> {
            ArrayList<CBeanDefinition> beans = new ArrayList<>();
            beans.add(new CAbstractBeanDefinition(Startup.class, CBeanDefinition.CBeanScope.PROTOTYPE) {
                @Override
                public String getBeanName() {
                    return "BCD";
                }
            });
            return beans;
        }, new CDefaultConstructorResolver());
        System.out.println(factory.getBean("BCD"));
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

    @Override
    public void setBeanFactory(CBeanFactory beanFactory) {
//        System.out.println("CALL");
    }

    @Override
    public void setBeanName(String name) {
        System.out.println(name);
    }
}
