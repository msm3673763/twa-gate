package com.ucsmy.commons.utils;

/**
 * 通用状态枚举类
 * Created by chenqilin on 2017/4/21.
 */
public enum CommStatusEnum {

    IN_USE("在用", "1"),
    UN_USE("停用", "0");

    private String value;

    private String name;

    private CommStatusEnum(String name, String value) {
        this.name = name;
        this.value = value;
    }

    public String getValue() {
        return value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}
