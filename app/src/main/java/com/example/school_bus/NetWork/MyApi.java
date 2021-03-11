package com.example.school_bus.NetWork;

import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.BusListData;
import com.example.school_bus.Entity.DriverStateData;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Entity.TestBus;
import com.example.school_bus.MyApp;
import com.example.school_bus.Utils.MyLog;

import java.util.Objects;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.MultipartBody;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;
import retrofit2.http.Query;

public class MyApi {
    private static String TAG = "MyApi";
    public final static String ServerBaseUrl = "http://8.129.224.197/";

    public interface Api {
        //注册
        @GET("school_bus/api/v1/register.php")
        Observable<UserData> register(@Query("username") String username, @Query("password") String password, @Query("phone") String phone);

        //token自动登录
        @GET("school_bus/api/v1/tokenLogin.php")
        Observable<UserData> tokenLogin();

        //账号密码或手机登陆
        @GET("school_bus/api/v1/login.php")
        Observable<UserData> login(@Query("username") String username, @Query("password") String password, @Query("phone") String phone, @Query("type") int type);

        //更换头像
        @Multipart
        @POST("school_bus/api/v1/upHeadImage.php")
        Observable<UserData> upHead(@Part MultipartBody.Part body);

        //上传位置信息
        @GET("school_bus/api/v1/upLocation.php")
        Observable<UserData> upLocation(@Query("latitude") double latitude, @Query("longitude") double longitude);

        //获取校车路线
        @GET("school_bus/api/v1/getBusWay.php")
        Observable<UserData> getBusWay(@Query("type") int type);

        //获取等待的车辆
        @GET("school_bus/api/v1/getAllWaitingBus.php")
        Observable<BusListData> getAllWaitingBus();

        //获取司机当前状态
        @GET("school_bus/api/v1/getDriverState.php")
        Observable<DriverStateData> getDriverState();

        //发车
        @GET("school_bus/api/v1/runBus.php")
        Observable<BaseData> runBus(@Query("id") String id, @Query("latitude") double latitude, @Query("longitude") double longitude);

        //到达
        @GET("school_bus/api/v1/arriveBus.php")
        Observable<BaseData> arriveBus(@Query("latitude") double latitude, @Query("longitude") double longitude);

        //行驶过程中位置上传
        @GET("school_bus/api/v1/upBusLocation.php")
        Observable<BaseData> upBusLocation(@Query("latitude") double latitude, @Query("longitude") double longitude);


        //---------------------------------------------test-----------------------------------------------------------

        //校车上传位置测试
        @GET("school_bus/api/v1/testInsertBus.php")
        Observable<UserData> testInsertBus(@Query("latitude") double latitude, @Query("longitude") double longitude);

        //获取校车路线测试
        @GET("school_bus/api/v1/testGetBus.php")
        Observable<TestBus> testGetBus();
    }

    public static Api createApi() {
        //Api日志
        Interceptor interceptor = chain -> {
            Request request = chain.request();
            long startTime = System.currentTimeMillis();
            Response response = chain.proceed(chain.request());
            long endTime = System.currentTimeMillis();
            long duration = endTime - startTime;
            MediaType mediaType = Objects.requireNonNull(response.body()).contentType();
            String content = response.body().string();
            MyLog.e(TAG, "----------------Request Start----------------");
            MyLog.e(TAG, "request：" + request.toString());
            MyLog.e(TAG, "headers：" + request.headers().toString());
            MyLog.e(TAG, "body：" + content);
            MyLog.e(TAG, "----------------Request End：用时" + duration + "毫秒----------------");
            return response.newBuilder()
                    .body(ResponseBody.create(mediaType, content))
                    .build();
        };
        //拦截器
        OkHttpClient okHttpClient = new OkHttpClient.Builder()
                .connectTimeout(35, TimeUnit.SECONDS)
                .writeTimeout(20, TimeUnit.SECONDS)
                .readTimeout(20, TimeUnit.SECONDS)
                .addInterceptor(chain -> {//添加头部信息
                    Request request = chain.request().newBuilder()
                            .addHeader("token", MyApp.getToken())
                            .method(chain.request().method(), chain.request().body())
                            .build();
                    return chain.proceed(request);
                })
                .addInterceptor(interceptor)
                .build();
        Retrofit retrofit = new Retrofit.Builder()
                .client(okHttpClient)
                .baseUrl(ServerBaseUrl)
                .addConverterFactory(MyGsonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        return retrofit.create(Api.class);
    }
}
