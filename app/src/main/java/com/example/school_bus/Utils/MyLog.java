package com.example.school_bus.Utils;

import android.text.TextUtils;
import android.util.Log;

public class MyLog {
    //读取BuildConfig.LOG_DEBUG 签名时为FALSE 不打印 debug时为true 打印
    public static void i(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Log.i(tag, message);
    }

    public static void d(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Log.d(tag, message);
    }

    public static void e(String tag, String message) {
        if (TextUtils.isEmpty(message)) {
            return;
        }
        Log.e(tag, message);
    }
}
