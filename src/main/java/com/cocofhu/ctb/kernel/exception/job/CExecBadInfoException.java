package com.cocofhu.ctb.kernel.exception.job;

import com.cocofhu.ctb.kernel.exception.CExecException;

public class CExecBadInfoException extends CExecException {

    private final String name;
    private final String group;
    private final String info;

    public CExecBadInfoException(String name, String group, String info) {
        super("incomplete exec info: (name: "+name+", info: "+info+", group: "+group+")");
        this.name = name;
        this.group = group;
        this.info = info;
    }

    public String getName() {
        return name;
    }

    public String getGroup() {
        return group;
    }

    public String getInfo() {
        return info;
    }
}
