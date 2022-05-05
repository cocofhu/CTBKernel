package com.cocofhu.ctb.kernel.convert;

/**
 * @author cocofhu
 */
public class IntegerConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(Integer.class == target.getClass() || Integer.TYPE == target.getClass()) return target;
        // Only
        return Integer.valueOf(target.toString());
    }
}
