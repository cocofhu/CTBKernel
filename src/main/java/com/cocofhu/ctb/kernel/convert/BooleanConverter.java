package com.cocofhu.ctb.kernel.convert;


/**
 * @author cocofhu 
 */
public class BooleanConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter should not be null.");
        if(Boolean.class == target.getClass() || Boolean.TYPE == target.getClass()) return target;
        if("false".equals(target)) return false;
        if("true".equals(target)) return true;
        throw new ConvertException(target + " of value unsupported.");
    }
}
