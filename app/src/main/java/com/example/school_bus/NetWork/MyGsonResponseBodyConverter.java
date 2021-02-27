package com.example.school_bus.NetWork;

import androidx.annotation.NonNull;

import com.example.school_bus.Entity.BaseData;
import com.google.gson.Gson;

import java.io.IOException;
import java.lang.reflect.Type;

import okhttp3.ResponseBody;
import retrofit2.Converter;

public class MyGsonResponseBodyConverter<T> implements Converter<ResponseBody, T> {

    private final Gson gson;
    private final Type type;

    public MyGsonResponseBodyConverter(Gson gson, Type type) {
        this.gson = gson;
        this.type = type;
    }

    @Override
    public T convert(@NonNull ResponseBody value) throws IOException {
        try {
            String response = value.string();
            BaseData baseData = gson.fromJson(response, BaseData.class);
            if (baseData.isSuccess()){
                return gson.fromJson(response, type);
            } else {
                //抛出自定义异常
                throw new MyServerException(baseData.getMessage(), baseData.getCode());
            }
        } finally {
            value.close();
        }
    }
}
