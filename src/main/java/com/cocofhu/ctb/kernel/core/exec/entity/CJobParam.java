package com.cocofhu.ctb.kernel.core.exec.entity;

import com.cocofhu.ctb.kernel.util.CCloneable;

import java.io.Serializable;
import java.util.Objects;

public class CJobParam implements CCloneable {

    // 参数类型
    private String name;
    // 参数说明
    private String info;
    // 参数是否可以为空
    private boolean nullable;
    // 参数类型 type
    private Object type;

    public CJobParam(String name, String info, boolean nullable, String type) {
        this.name = name;
        this.info = info;
        this.nullable = nullable;
        this.type = type;
    }

    public CJobParam(String name, String info, boolean nullable, Class<?> type) {
        this.name = name;
        this.info = info;
        this.nullable = nullable;
        this.type = type;
    }

    public CJobParam(String name, String info, Class<?> type) {
        this.name = name;
        this.info = info;
        this.nullable = false;
        this.type = type;
    }

    public CJobParam(String name, String info, String type) {
        this.name = name;
        this.info = info;
        this.nullable = false;
        this.type = type;
    }


    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Object getType() {
        return type;
    }

    public void setType(Object type) {
        this.type = type;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        CJobParam cJobParam = (CJobParam) o;
        return Objects.equals(name, cJobParam.name) && Objects.equals(type, cJobParam.type);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, type);
    }

    @Override
    public String toString() {
        return "CJobParam{" +
                "name='" + name + '\'' +
                ", info='" + info + '\'' +
                ", nullable=" + nullable +
                ", type=" + type +
                '}';
    }
}
