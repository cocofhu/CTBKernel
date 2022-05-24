package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.util.CReflectionUtils;
import java.lang.reflect.Method;
import java.util.Map;

public class  Executor {
    private final Class<?> clazz;
    private final String methodName;
    private final Map<String,Object> args;


    public Executor(Class<?> clazz, String methodName, Map<String, Object> args) {
        this.clazz = clazz;
        this.methodName = methodName;
        this.args = args;
    }

    public Object execute() throws Exception {
        Method method = CReflectionUtils.findMethod(clazz, methodName, null);
        if (method == null) {
            throw new NoSuchMethodException(clazz.toString() +"." + methodName  + " does not exist.");
        }
        Object obj = clazz.newInstance();
        Object[] methodParamList = CReflectionUtils.resolveMethodParamList(method, args);

        return CReflectionUtils.invokeMethod(method,obj,methodParamList);
    }
}
