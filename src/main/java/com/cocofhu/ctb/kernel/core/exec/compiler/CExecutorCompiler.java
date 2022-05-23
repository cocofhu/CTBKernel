package com.cocofhu.ctb.kernel.core.exec.compiler;


import com.cocofhu.ctb.kernel.core.exec.entity.CExecutorDefinition;
import com.cocofhu.ctb.kernel.exception.compiler.CBadSyntaxException;
import com.cocofhu.ctb.kernel.util.CStringUtils;
import com.cocofhu.ctb.kernel.util.ds.CDefaultDefaultWritableDataSet;
import com.cocofhu.ctb.kernel.util.ds.CPair;


import java.util.Arrays;
import java.util.function.BiFunction;


public interface CExecutorCompiler {
    default CExecutorDefinition compiler(String expression) {
        BiFunction<CPair<String,Character>,Integer, CPair<String,Integer>> fn = ((pair, i) -> {
            String src = pair.getFirst();
            Character target = pair.getSecond();
            boolean escape = false;
            boolean finished = false;
            if(i >= src.length()){
                throw new CBadSyntaxException("incomplete expression :" + src + " at position " + i);
            }
            StringBuilder sb = new StringBuilder();
            while(i < src.length() && !finished){
                char ch = src.charAt(i);
                if(escape || ch != target){
                    escape = false;
                    sb.append(ch);
                } else if(ch == '\\') {
                    escape = true;
                }else{
                    finished = true;
                }
                ++i;
            }

            if(finished || target == ' '){
                // 任意一个Token被解析出来时 都要删除多余的空格，去除多余的空格
                while(i < src.length() && src.charAt(i) == ' ')  ++i;
                return new CPair<>(sb.toString(),i);
            }
            throw new CBadSyntaxException("incomplete expression :" + src + " at position " + i);

        });


        String[] execs = expression.split(">");
        if(execs.length == 1){
            String exec = execs[0].trim();
            int i = 0 ;
            CPair<String, Integer> pair = fn.apply(new CPair<>(exec, ' '), i);
            i = pair.getSecond();
            String execName = pair.getFirst();

            // 调整附加参数 这里是
            CExecutorDefinition definition = acquireNewExecutorDefinition(execName);
            CDefaultDefaultWritableDataSet<String, Object> attachment = new CDefaultDefaultWritableDataSet<>(definition.getAttachment());
            definition.setAttachment(attachment);

            String key = null;
            String val = "val";

            while(i < exec.length()){
                char ch = exec.charAt(i);
                boolean isKey = false;
                if(ch == '-'){
                    pair = fn.apply(new CPair<>(exec, ' '), i + 1);
                    isKey = true;
                } else if(ch == '\'') {
                    pair = fn.apply(new CPair<>(exec, '\''), i + 1);
                } else if(ch == '\"'){
                    pair = fn.apply(new CPair<>(exec, '\"'), i + 1);
                }else{
                    pair = fn.apply(new CPair<>(exec, ' '), i);
                }
                if(isKey){
                    if(val == null){
                        throw new CBadSyntaxException("except argument value, but argument name found, " + exec + " at position " + i);
                    }
                    key = pair.getFirst();
                    val = null;
                }else{
                    if(key == null){
                        throw new CBadSyntaxException("except argument name, but argument value found, " + exec + " at position " + i);
                    }
                    val = pair.getFirst();
                    attachment.put(key,val);
                    key = null;
                }
                i = pair.getSecond();
            }

            // check it finally!
            if(key != null){
                throw new CBadSyntaxException("except argument value, but argument name found, " + exec + " at position " + i);
            }

            return definition;
        }

        CExecutorDefinition[] definitions = new CExecutorDefinition[execs.length];
        for (int i = 0; i < execs.length; i++) {
            String exec = execs[i].trim();
            if(CStringUtils.isEmpty(exec)){
                throw new CBadSyntaxException("incomplete expression : at segment " + i);
            }
            definitions[i] = compiler(exec);
        }

        return new CExecutorDefinition("","","",definitions,null);
    }

    CExecutorDefinition acquireNewExecutorDefinition(String nameOrAlias);
}
