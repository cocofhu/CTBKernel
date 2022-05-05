package com.cocofhu.ctb.kernel.convert;

/**
 * @author cocofhu
 */
public class LongConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(Long.class == target.getClass() || Long.TYPE == target.getClass()) return target;
        // Only
        return Long.valueOf(target.toString());
    }
}
