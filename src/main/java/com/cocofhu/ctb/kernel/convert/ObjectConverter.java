package com.cocofhu.ctb.kernel.convert;


/**
 * @author cocofhu 
 */
public class ObjectConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter should not be null.");
        return target;
    }
}
