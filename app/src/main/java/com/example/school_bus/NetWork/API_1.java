package com.example.school_bus.NetWork;

import android.os.Build;
import android.util.Log;

import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Utils.HttpLoggerUtils;
import com.example.school_bus.Utils.MyToast;
import com.google.gson.Gson;

import java.io.IOException;
import java.io.InputStream;
import java.security.KeyStore;
import java.security.SecureRandom;
import java.security.cert.CertificateFactory;
import java.util.Collections;
import java.util.concurrent.TimeUnit;

import javax.net.ssl.HostnameVerifier;
import javax.net.ssl.SSLContext;
import javax.net.ssl.SSLSession;
import javax.net.ssl.TrustManager;
import javax.net.ssl.TrustManagerFactory;
import javax.net.ssl.X509TrustManager;

import io.reactivex.Observable;
import okhttp3.Interceptor;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;
import okhttp3.Protocol;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.http.GET;
import retrofit2.http.Query;

/**
 * Created by hy003 on 2017/8/17.
 */

public class API_1 {

    public final static String ServerBaseUrl = "https://api.apiopen.top/";

    private static String TAG = "ApiRetrofit";

    private static Retrofit retrofit;

    public interface Api {
        int API_ACCESS_SUCCESS = 200;

        //获取新闻
        @GET("getWangYiNews")
        Observable<NewsData> getNews(@Query("page") String page, @Query("count") String count);
    }



    public static Api createApi() {

        HttpLoggingInterceptor interceptor = new HttpLoggingInterceptor(new HttpLoggerUtils());
        interceptor.setLevel(HttpLoggingInterceptor.Level.BODY);

        /**
         * 请求访问quest
         * response拦截器
         */
        Interceptor interceptor1 = null;
        try {
            interceptor1 = new Interceptor() {
                @Override
                public Response intercept(Chain chain) throws IOException {
                    Request request = chain.request();
                    long startTime = System.currentTimeMillis();
                    Response response = chain.proceed(chain.request());
                    long endTime = System.currentTimeMillis();
                    long duration = endTime - startTime;
                    MediaType mediaType = response.body().contentType();
                    String content = response.body().string();
                    Log.e(TAG, "----------Request Start----------------");
                    Log.e(TAG, "| " + request.toString() + request.headers().toString());
                    Log.e(TAG, "| Response:" + content);
                    Log.e(TAG, "----------Request End:" + duration + "毫秒----------");
                    return response.newBuilder()
                            .body(ResponseBody.create(mediaType, content))
                            .build();
                }
            };
        } catch (Exception e) {
            e.printStackTrace();
        }

        retrofit = new Retrofit.Builder()
//        其中BaseUrl的结尾必须为“/”，不然请求链接时会报错。
                .baseUrl(ServerBaseUrl)
                .addConverterFactory(LenientGsonConverterFactory.create(new Gson()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();

        return retrofit.create(Api.class);
    }


    static class MyVerifier implements HostnameVerifier {

        @Override
        public boolean verify(String hostname, SSLSession session) {
            return true;
        }
    }


    static TrustManager[] trustAllCerts = new TrustManager[]{new X509TrustManager() {
        @Override
        public void checkClientTrusted(
                java.security.cert.X509Certificate[] x509Certificates,
                String s) throws java.security.cert.CertificateException {
        }

        @Override
        public void checkServerTrusted(
                java.security.cert.X509Certificate[] x509Certificates,
                String s) throws java.security.cert.CertificateException {
        }

        @Override
        public java.security.cert.X509Certificate[] getAcceptedIssuers() {
            return new java.security.cert.X509Certificate[]{};
        }
    }};

    public static void setCertificates(OkHttpClient.Builder mOkHttpClient, InputStream... certificates) {
        try {
            CertificateFactory certificateFactory = CertificateFactory.getInstance("X.509");
            KeyStore keyStore = KeyStore.getInstance(KeyStore.getDefaultType());
            keyStore.load(null);
            int index = 0;
            for (InputStream certificate : certificates) {
                String certificateAlias = Integer.toString(index++);
                keyStore.setCertificateEntry(certificateAlias, certificateFactory.generateCertificate(certificate));

                try {
                    if (certificate != null)
                        certificate.close();
                } catch (IOException e) {
                }
            }

            SSLContext sslContext = SSLContext.getInstance("TLS");

            TrustManagerFactory trustManagerFactory =
                    TrustManagerFactory.getInstance(TrustManagerFactory.getDefaultAlgorithm());

            trustManagerFactory.init(keyStore);
            sslContext.init
                    (
                            null,
                            trustManagerFactory.getTrustManagers(),
                            new SecureRandom()
                    );
            mOkHttpClient.sslSocketFactory(sslContext.getSocketFactory());


        } catch (Exception e) {
            e.printStackTrace();
        }

    }
}
