package com.example.school_bus.Utils;

import okhttp3.OkHttpClient;
import okhttp3.Request;

public class HttpUtil {
    //发起一条HTTP请求方法，传入请求地址，注册一个回调处理服务器响应
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client=new OkHttpClient();
        Request request=new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }
}
