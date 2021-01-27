package com.example.school_bus.Utils;

import androidx.annotation.NonNull;

import com.example.school_bus.NetWork.API_login;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 15:02
 * @类名 ImageUtil
 * @所在包 com\example\school_bus\Utils\ImageUtil.java
 * 图片工具类
 */
public class ImageUtil {

    public static String head = API_login.ServerBaseUrl + "school_bus/api/v1/upload/";

    /**
     * 获取头像的真实地址
     * @param source 原始地址
     * @return
     */
    public static String getHeadUrl(@NonNull String source){
        return head + source;
    }
}
