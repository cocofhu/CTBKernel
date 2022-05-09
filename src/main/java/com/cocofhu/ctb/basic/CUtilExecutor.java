package com.cocofhu.ctb.basic;

import com.cocofhu.ctb.kernel.anno.param.CExecutorInput;

import java.io.BufferedReader;
import java.io.FileReader;


public class CUtilExecutor {



    public String readText(@CExecutorInput String source) throws Exception {
        try (
                BufferedReader br = new BufferedReader(new FileReader(source))
        ) {
            StringBuilder sb = new StringBuilder();
            String line;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

}
