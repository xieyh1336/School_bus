package com.example.school_bus.NetWork;

import com.google.gson.Gson;
import com.google.gson.TypeAdapter;
import com.google.gson.reflect.TypeToken;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import okhttp3.RequestBody;
import okhttp3.ResponseBody;
import retrofit2.Converter;
import retrofit2.Retrofit;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-27 16:14
 * @类名 MyGsonConverterFactory
 * @所在包 com\example\school_bus\NetWork\MyGsonConverterFactory.java
 * 自定义数据解析类
 */
public class MyGsonConverterFactory extends Converter.Factory {

    private final Gson gson;

    public static MyGsonConverterFactory create(){
        return create(new Gson());
    }

    public static MyGsonConverterFactory create(Gson gson){
        return new MyGsonConverterFactory(gson);
    }

    public MyGsonConverterFactory(Gson gson) {
        if (gson == null){
            throw new NullPointerException("gson == null");
        }
        this.gson = gson;
    }

    @Override
    public Converter<ResponseBody, ?> responseBodyConverter(Type type, Annotation[] annotations, Retrofit retrofit) {
        return new MyGsonResponseBodyConverter<>(gson, type);
    }

    @Override
    public Converter<?, RequestBody> requestBodyConverter(Type type, Annotation[] parameterAnnotations, Annotation[] methodAnnotations, Retrofit retrofit) {
        TypeAdapter<?> adapter = gson.getAdapter(TypeToken.get(type));
        return new MyGsonRequestBodyConverter<>(gson, adapter);
    }
}
