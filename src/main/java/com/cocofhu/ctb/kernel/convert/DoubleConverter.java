package com.cocofhu.ctb.kernel.convert;

public class DoubleConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(Double.class == target.getClass() || Double.TYPE == target.getClass()) return target;
        // Only
        return Double.valueOf(target.toString());
    }
}
