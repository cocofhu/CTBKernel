package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.util.ReflectionUtils;
import java.lang.reflect.Method;
import java.util.Map;

public class Executor {
    private Class<?> clazz;
    private String methodName;
    private Map<String,Object> args;


    public Executor(Class<?> clazz, String methodName, Map<String, Object> args) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.args = args;
    }

    public Object execute() throws Exception {
        Method method = ReflectionUtils.findMethod(clazz, methodName, null);
        if (method == null) {
            throw new NoSuchMethodException(clazz.toString() +"." + methodName  + " does not exist.");
        }
        Object obj = clazz.newInstance();
        Object[] methodParamList = ReflectionUtils.resolveMethodParamList(method, args);

        return ReflectionUtils.invokeMethod(method,obj,methodParamList);
    }
}