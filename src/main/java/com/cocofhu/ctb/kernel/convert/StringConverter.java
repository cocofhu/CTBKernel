package com.cocofhu.ctb.kernel.convert;

/**
 * @author cocofhu
 */
public class StringConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(target.getClass() == String.class) return target;
        throw new IllegalArgumentException("Cannot Conversion");
    }
}
