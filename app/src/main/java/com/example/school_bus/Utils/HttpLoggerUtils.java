package com.example.school_bus.Utils;

import okhttp3.logging.HttpLoggingInterceptor.Logger;

public class HttpLoggerUtils implements Logger {
    @Override
    public void log(String message) {
        MyLog.d("HttpLogInfo", message);
    }
}
