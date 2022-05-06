package com.cocofhu.ctb.basic.entity;

public class JobParam {

    private String name;
    private String info;
    private String value;
    private boolean nullable;
    private Class<?> type;

    public JobParam(String name, String info, String value, boolean nullable, Class<?> type) {
        this.name = name;
        this.info = info;
        this.value = value;
        this.nullable = nullable;
        this.type = type;
    }

    public JobParam() {
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

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public boolean isNullable() {
        return nullable;
    }

    public void setNullable(boolean nullable) {
        this.nullable = nullable;
    }

    public Class<?> getType() {
        return type;
    }

    public void setType(Class<?> type) {
        this.type = type;
    }
}
