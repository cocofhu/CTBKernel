package com.cocofhu.ctb.kernel.convert;

public class ByteConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter should not be null.");
        if(target.getClass() == Byte.class || target.getClass() == Byte.TYPE) return target;


        return Byte.valueOf(target.toString());
    }
}
