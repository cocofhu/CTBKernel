package com.cocofhu.ctb.kernel.util;

import java.io.*;

public abstract class CStringUtils {
    public static boolean isEmpty(CharSequence sequence){
        return sequence == null || sequence.length() == 0;
    }
}
