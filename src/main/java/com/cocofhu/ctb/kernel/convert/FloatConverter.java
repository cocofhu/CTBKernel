package com.cocofhu.ctb.kernel.convert;

/**
 * @author cocofhu
 */
public class FloatConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(Float.class == target.getClass() || Float.TYPE == target.getClass()) return target;
        // Only
        return Float.valueOf(target.toString());
    }
}
