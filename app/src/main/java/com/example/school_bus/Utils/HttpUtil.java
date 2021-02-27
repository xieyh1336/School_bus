package com.example.school_bus.Utils;

import android.content.Context;
import android.widget.Toast;

import com.example.school_bus.NetWork.MyServerException;

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

    private static String TAG = "HttpUtil";
    //发起一条HTTP请求方法，传入请求地址，注册一个回调处理服务器响应
    public static void sendOkHttpRequest(String address, okhttp3.Callback callback){
        OkHttpClient client = new OkHttpClient();
        Request request = new Request.Builder().url(address).build();
        client.newCall(request).enqueue(callback);
    }

    public static void onError(Throwable e){
        if (e.getMessage() != null){
            MyLog.e(TAG, "错误信息：" + e.getMessage());
        }
        if (e instanceof MyServerException) {
            MyLog.e(TAG, "错误码：" + ((MyServerException) e).getErrorCode());
            switch (((MyServerException) e).getErrorCode()){
                case MyServerException.TOKEN_IS_EMPTY:
                    break;
                case MyServerException.TOKEN_ERROR:
                    MyToast.showToast("登录信息已失效，请重新登录");
                    break;
                case MyServerException.TOKEN_IS_EXPIRE:
                    MyToast.showToast("长时间未登录，请重新登录");
                    break;
                case MyServerException.TOKEN_CHECK_FAIL1:
                case MyServerException.TOKEN_CHECK_FAIL2:
                    MyToast.showToast("未知错误");
                    break;
            }
        } else if (e instanceof HttpException){
            MyToast.showToast("服务器异常，请稍后重试");
        } else {
            MyToast.showToast("未知错误");
        }
    }
}
