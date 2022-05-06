package com.cocofhu.ctb.basic;

import com.alibaba.fastjson.JSON;
import com.cocofhu.ctb.basic.entity.JobDetail;
import com.cocofhu.ctb.kernel.anno.CAttachmentArgs;

import java.io.BufferedReader;
import java.io.FileNotFoundException;
import java.io.FileReader;


public class CUtilExecutor {



    public String readText(@CAttachmentArgs String source) throws Exception {
        try (
                BufferedReader br = new BufferedReader(new FileReader(source));
        ) {
            StringBuilder sb = new StringBuilder();
            String line = null;
            while ((line = br.readLine()) != null) {
                sb.append(line).append("\n");
            }
            return sb.toString();
        }
    }

}
