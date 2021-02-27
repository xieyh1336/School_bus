package com.example.school_bus.NetWork;

import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Entity.PicturesData;
import com.example.school_bus.Utils.HttpLoggerUtils;
import com.google.gson.Gson;

import io.reactivex.Observable;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class API_1 {

    public final static String ServerBaseUrl = "https://api.apiopen.top/";

    private static Retrofit retrofit;

    public interface Api {
        //获取网易新闻
        @GET("getWangYiNews")
        Observable<NewsData> getNews(@Query("page") String page, @Query("count") String count);

        //获取美图推荐，该接口返回的图片有问题，暂时不使用
        @GET("getImages")
        Observable<PicturesData> getPictures(@Query("page") String page, @Query("count") String count);
    }

    public static Api createApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggerUtils());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .baseUrl(ServerBaseUrl)
                .addConverterFactory(GsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }
}
