package com.cocofhu.ctb.kernel.core.factory;

import com.cocofhu.ctb.kernel.core.config.CExecutableWrapper;
import com.cocofhu.ctb.kernel.core.config.CTBContext;
import com.cocofhu.ctb.kernel.core.factory.exec.CExecutorContext;
import com.cocofhu.ctb.kernel.core.factory.exec.CExecutorMethod;
import com.cocofhu.ctb.kernel.core.resolver.CProcess;
import com.cocofhu.ctb.kernel.anno.param.CBeanRefProcess;
import com.cocofhu.ctb.kernel.core.creator.CDefaultBeanInstanceCreator;
import com.cocofhu.ctb.kernel.core.resolver.bean.CBeanDefinitionResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.ctor.CDefaultNoParameterConstructorResolver;
import com.cocofhu.ctb.kernel.core.resolver.value.*;
import com.cocofhu.ctb.kernel.exception.CExecutorExceptionUnhandledException;
import com.cocofhu.ctb.kernel.exception.CMethodBeanInvokeException;
import com.cocofhu.ctb.kernel.exception.CNoSuchMethodException;
import com.cocofhu.ctb.kernel.util.ReflectionUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;


public class CMethodBeanFactory extends CDefaultBeanFactory {


    public static final String EXEC_CONTEXT_KEY = "EXEC_CONTEXT_KEY";
    public static final String EXEC_RETURN_VAL_KEY = "EXEC_RETURN_VAL_KEY";
    public static final String EXEC_EXCEPTION_KEY = "EXEC_EXCEPTION_KEY";


    @SuppressWarnings("unchecked")
    public CMethodBeanFactory(CBeanDefinitionResolver beanDefinitionResolver) {
        super(new CDefaultBeanInstanceCreator(new CConstructorResolver[]{new CDefaultConstructorResolver(), new CDefaultNoParameterConstructorResolver()}),
                beanDefinitionResolver,
                new CChainValueResolver(
                        new CValueResolver[]{
                                new CAnnotationValueResolver(new CProcess[]{new com.cocofhu.ctb.kernel.anno.param.CValueProcess(), new CBeanRefProcess()}),
                                new CExecutorInputValueResolver(),
                                new CBeanFactoryValueResolver()
                        })
        );
    }

    public void invokeMethod(CExecutorMethod executorMethod, CExecutorContext executorContext) {
        Object bean = getBean(executorMethod.getBeanName(), executorMethod.getBeanClass());
        Method method = ReflectionUtils.findMethod(bean.getClass(), executorMethod.getMethodName(), executorMethod.getParameterTypes());
        if (method == null) {
            throw new CNoSuchMethodException(executorMethod.getBeanName() + "." + executorMethod.getMethodName(), executorMethod.getParameterTypes());
        }
        CTBContext ctx = context.newCTBContext();
        ctx.put(EXEC_CONTEXT_KEY, executorContext);
        CExecutableWrapper executableWrapper = new CExecutableWrapper(method, ctx);
        try {
            Object returnVal = executableWrapper.execute(bean);
            executorContext.put(EXEC_RETURN_VAL_KEY, returnVal);
        } catch (InstantiationException | IllegalAccessException e) {
            throw new CMethodBeanInvokeException("cannot call method of " + method.getName() + ", exception message : " + e.getMessage());
        } catch (InvocationTargetException e) {
            executorContext.put(EXEC_EXCEPTION_KEY,e.getTargetException());
        }
    }





}
