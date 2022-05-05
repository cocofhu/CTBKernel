package com.cocofhu.ctb.kernel.convert;

import java.util.HashMap;
import java.util.Map;

/**
 * @author cocofhu
 */
public class ConverterUtils {
    private static final Map<Class<?>,IConverter> converters = new HashMap<>();
    static {
        converters.put(Integer.class,new IntegerConverter());
        converters.put(Integer.TYPE,new IntegerConverter());
        converters.put(Long.class,new LongConverter());
        converters.put(Long.TYPE,new LongConverter());
        converters.put(Short.class,new ShortConverter());
        converters.put(Short.TYPE,new ShortConverter());
        converters.put(Byte.class,new ByteConverter());
        converters.put(Byte.TYPE,new ByteConverter());

        converters.put(Character.class,new CharacterConverter());
        converters.put(Character.TYPE,new CharacterConverter());



        converters.put(Double.class,new DoubleConverter());
        converters.put(Double.TYPE,new DoubleConverter());
        converters.put(Float.class,new FloatConverter());
        converters.put(Float.TYPE,new FloatConverter());

        converters.put(Boolean.class,new BooleanConverter());
        converters.put(Boolean.TYPE,new BooleanConverter());

        converters.put(String.class,new StringConverter());
    }

    public static Object convert(Object raw, Class<?> type){
        return converters.get(type).convert(raw);
    }

}
