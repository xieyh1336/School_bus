package com.example.school_bus.Utils;

import android.content.Context;
import android.widget.Toast;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import retrofit2.HttpException;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 16:50
 * @类名 HttpUtil
 * @所在包 com\example\school_bus\Utils\HttpUtil.java
 * http工具类
 */
public class HttpUtil {
    //发起一条HTTP请求方法，传入请求地址，注册一个回调处理服务器响应
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void onError(Context context, Throwable e){
        if (e instanceof HttpException) {
            Toast.makeText(context, "服务器异常，请稍后重试", Toast.LENGTH_SHORT).show();
        } else {
            Toast.makeText(context, "网络异常，请检查网络", Toast.LENGTH_SHORT).show();
        }
    }
}
