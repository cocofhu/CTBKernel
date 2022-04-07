package com.cocofhu.ctb.kernel;

import com.cocofhu.ctb.kernel.core.CSimpleBean;
import com.cocofhu.ctb.kernel.core.ICBeanBehavior;

import java.lang.reflect.InvocationTargetException;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

public class CBeanFactory {
    private Map<String, ICBeanBehavior> beans = new ConcurrentHashMap<>();

    public void registerBean(String name,Class<?> clazz) {
        try {
            beans.put(name,new CSimpleBean(clazz, ICBeanBehavior.BeanScope.SINGLETON));
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
    }

    public Object getBean(String name) {
        try {
            return beans.get(name).getInstance();
        } catch (InvocationTargetException | InstantiationException | IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

}
