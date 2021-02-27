package com.example.school_bus.NetWork;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-27 16:05
 * @类名 MyServerException
 * @所在包 com\example\school_bus\NetWork\MyServerException.java
 * 我的Api访问失败异常类
 */
public class MyServerException extends RuntimeException {

    //token为空
    public static final int TOKEN_IS_EMPTY = 30001;

    //token错误
    public static final int TOKEN_ERROR = 30002;

    //token长时间未使用
    public static final int TOKEN_IS_EXPIRE = 30003;

    //token验证失败
    public static final int TOKEN_CHECK_FAIL1 = 30004;

    //token验证失败
    public static final int TOKEN_CHECK_FAIL2 = 30005;

    private int errorCode;

    public MyServerException(String message, int errorCode) {
        super(message);
        this.errorCode = errorCode;
    }

    public int getErrorCode(){
        return errorCode;
    }
}
