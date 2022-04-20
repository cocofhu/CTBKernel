package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.anno.CBean;
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
import com.cocofhu.ctb.kernel.core.resolver.bean.CClassPathAnnotationBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.CAnnotationValueResolver;

import java.lang.reflect.Method;
import java.util.ArrayList;

@CBean("Startup")
public class Startup implements CBeanFactoryAware , CBeanNameAware {

    public String f(boolean x,int y){
        System.out.println(y);
        return x + "" + y;
    }

    public Startup(@CValue("555") int x,@CValue("184.678901") double y, @CBeanRef("Person") Person person){
        System.out.println(x);
        System.out.println(y);
        System.out.println(person);
    }

    @CBean("Person")
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

    @CBean("Derived")
    public static class Derived extends Person{

        public Derived(@CValue("4444") int x, @CValue("555") int y) {
            super(x, y);
        }

        public void omg(@CValue("9999999") int x){
            System.out.println("init  " + x);
        }

        @Override
        public String toString() {
            return super.toString() + "DDDDD";
        }
    }



    public static void main(String[] args) throws Exception {
        // 默认的factory
        CDefaultBeanFactory factory =
                new CDefaultBeanFactory(new CDefaultBeanInstanceCreator(
                        new CConstructorResolver[]{new CDefaultConstructorResolver(),new CDefaultNoParameterConstructorResolver()}),
                        new CClassPathAnnotationBeanDefinitionResolver(),
                        new CAnnotationValueResolver(new CAnnotationProcess[]{new CValueProcess(),new CBeanRefProcess()})
                );
        System.out.println(factory.getBean("Derived"));
        System.out.println(factory.getBean("Startup"));
//        System.out.println(factory.getBean(Object.class));

        new CClassPathAnnotationBeanDefinitionResolver().resolveAll();
//        System.out.println(Person.class.isAssignableFrom(null));
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
