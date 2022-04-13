package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.anno.process.CAnnotationProcess;
import com.cocofhu.ctb.kernel.anno.process.param.CBeanRef;
import com.cocofhu.ctb.kernel.anno.process.param.CBeanRefProcess;
import com.cocofhu.ctb.kernel.anno.process.param.CValue;
import com.cocofhu.ctb.kernel.anno.process.param.CValueProcess;
import com.cocofhu.ctb.kernel.core.aware.CBeanFactoryAware;
import com.cocofhu.ctb.kernel.core.aware.CBeanNameAware;
import com.cocofhu.ctb.kernel.core.config.CAbstractBeanDefinition;
import com.cocofhu.ctb.kernel.core.config.CBeanDefinition;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.factory.CBeanFactory;
import com.cocofhu.ctb.kernel.core.factory.CDefaultBeanFactory;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;

import java.util.ArrayList;

public class Startup implements CBeanFactoryAware , CBeanNameAware {

    public String f(boolean x,int y){
        System.out.println(y);
        return x + "" + y;
    }

    public Startup(@CValue("555") int x,@CValue("184.678901") double y, @CBeanRef("person") Person person){
        System.out.println(x);
        System.out.println(y);
        System.out.println(person);
    }

    public static class Person{
        public int x;
        public int y;
        public Person(@CValue("100") int x,@CValue("200") int y){
            this.x = x;
            this.y = y;
//            System.out.println(x+y);
        }

        @Override
        public String toString() {
            return "Person{" +
                    "x=" + x +
                    ", y=" + y +
                    '}';
        }
    }



    public static void main(String[] args) throws Exception {
        // 默认的factory
        CDefaultBeanFactory factory = new CDefaultBeanFactory(new CDefaultBeanInstanceCreator(), () -> {
            ArrayList<CBeanDefinition> beans = new ArrayList<>();
            beans.add(new CAbstractBeanDefinition(Startup.class, CBeanDefinition.CBeanScope.SINGLETON) {
                @Override
                public String getBeanName() {
                    return "BCD";
                }
            });
            beans.add(new CAbstractBeanDefinition(Person.class, CBeanDefinition.CBeanScope.SINGLETON) {
                @Override
                public String getBeanName() {
                    return "person";
                }
            });
            return beans;
        }, new CAnnotationProcess[]{new CValueProcess(), new CBeanRefProcess()}, new CConstructorResolver[]{new CDefaultNoParameterConstructorResolver(), new CDefaultConstructorResolver()});
        System.out.println(factory.getBean("BCD"));
        System.out.println(factory.getBean("BCD"));
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
