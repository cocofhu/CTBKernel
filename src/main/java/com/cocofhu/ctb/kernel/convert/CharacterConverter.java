package com.cocofhu.ctb.kernel.convert;

/**
 * @author cocofhu
 */
public class CharacterConverter implements IConverter{
    public Object convert(Object target){
        if(target == null) throw new IllegalArgumentException("target parameter is null");
        if(target.getClass() == Character.class || target.getClass() == Character.TYPE) return target;


        if(target instanceof String && ((String) target).length() == 1){
            return ((String) target).charAt(0);
        }

        throw new IllegalArgumentException("Cannot Conversion");
    }
}
