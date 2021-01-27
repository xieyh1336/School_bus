package com.example.school_bus.Utils;

import androidx.annotation.NonNull;

import okhttp3.logging.HttpLoggingInterceptor.Logger;

public class HttpLoggerUtils implements Logger {
    @Override
    public void log(@NonNull String message) {
        MyLog.e("HttpLogInfo", message);
    }
}
