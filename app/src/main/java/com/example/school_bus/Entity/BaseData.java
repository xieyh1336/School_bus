package com.example.school_bus.Entity;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 13:52
 * @类名 BaseData
 * @所在包 com\example\school_bus\Entity\BaseData.java
 * 基础数据类
 */
public class BaseData {

    /**
     * code : 20000
     * message : 登陆成功
     */

    private int code;
    private String message;

    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
