package com.example.school_bus.Utils;

import okhttp3.logging.HttpLoggingInterceptor.Logger;

/**
 * Created by Administrator on 2018/12/27.
 */

public class HttpLoggerUtils implements Logger {
    @Override
    public void log(String message) {
        MyLog.d("HttpLogInfo", message);
    }
}
