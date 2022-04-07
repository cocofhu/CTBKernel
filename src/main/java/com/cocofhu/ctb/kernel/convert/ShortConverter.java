package com.cocofhu.ctb.kernel.convert;

public class ShortConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(Short.class == target.getClass() || Short.TYPE == target.getClass()) return target;

        // Only
        return Short.valueOf(target.toString());
    }
}
