package com.example.school_bus.NetWork;

import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Entity.PicturesData;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Utils.HttpLoggerUtils;
import com.google.gson.Gson;

import io.reactivex.Observable;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

public class API_login {
    public final static String ServerBaseUrl = "http://8.129.224.197/";

    private static Retrofit retrofit;

    public interface Api {
        //注册
        @GET("school_bus/api/v1/register.php")
        Observable<UserData> register(@Query("username") String username, @Query("password") String password, @Query("phone") String phone);

        //token自动登录
        @GET("school_bus/api/v1/tokenLogin.php")
        Observable<UserData> tokenLogin(@Query("token") String token);

        //账号密码或手机登陆
        @GET("school_bus/api/v1/login.php")
        Observable<UserData> login(@Query("username") String username, @Query("password") String password, @Query("phone") String phone, @Query("type") int type);
    }

    public static Api createApi() {
        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggerUtils());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);
        retrofit = new Retrofit.Builder()
                .baseUrl(ServerBaseUrl)
                .addConverterFactory(LenientGsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }
}
