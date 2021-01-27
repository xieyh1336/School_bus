package com.example.school_bus;

import android.app.Application;
import android.content.Context;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.StrictMode;

import androidx.appcompat.app.AppCompatDelegate;

import com.example.school_bus.Utils.DynamicTimeFormat;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.footer.ClassicsFooter;
import com.scwang.smartrefresh.layout.header.ClassicsHeader;
import com.squareup.leakcanary.LeakCanary;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-06 17:58
 * @类名 MyApp
 * @所在包 com\example\school_bus\MyApp.java
 * 定制化的Application
 */
public class MyApp extends Application {

    private static String TAG = "MyApp";
    private static SharedPreferences preferences;//存储用户基本信息
    private static Context context;

    @Override
    public void onCreate() {
        super.onCreate();
        //防止内存泄漏框架
        if (LeakCanary.isInAnalyzerProcess(this)){
            return;
        }
        LeakCanary.install(this);

        context = getApplicationContext();
        preferences = context.getSharedPreferences("user", Context.MODE_PRIVATE);

        //兼容Android7.0拍照闪退
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N){
            StrictMode.VmPolicy.Builder builder = new StrictMode.VmPolicy.Builder();
            StrictMode.setVmPolicy(builder.build());
        }
    }

    public static Context getContext(){
        return context;
    }

    public static String getToken(){
        return preferences.getString("token", "");
    }

    public static void clearToken(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("token");
        editor.apply();
    }

    public static String getHead(){
        return preferences.getString("head", "");
    }

    public static void clearHead(){
        SharedPreferences.Editor editor = preferences.edit();
        editor.remove("head");
        editor.apply();
    }

    static {
        //智能刷新框架
        AppCompatDelegate.setCompatVectorFromResourcesEnabled(true);//启用矢量图兼容
        //设置智能刷新框架默认配置
        SmartRefreshLayout.setDefaultRefreshInitializer((context, layout) -> {
            layout.setEnableAutoLoadMore(false);//是否启用列表惯性滑动到底部时自动加载更多
            layout.setEnableOverScrollDrag(true);//是否启用越界拖动（仿苹果效果）1.0.4
            layout.setEnableOverScrollBounce(false);//是否启用越界回弹
            layout.setEnableLoadMoreWhenContentNotFull(true);//是否在列表不满一页时候开启上拉加载功能
            layout.setEnableScrollContentWhenRefreshed(true);//是否在刷新完成时滚动列表显示新的内容
            layout.setPrimaryColorsId(R.color.colorPrimary, android.R.color.white);
            layout.setFooterMaxDragRate(4.0F);//最大显示下拉高度/Footer标准高度
            layout.setFooterHeight(45);//Footer标准高度（显示上拉高度>=标准高度 触发加载）
        });
        //刷新头部
        SmartRefreshLayout.setDefaultRefreshHeaderCreator((context, layout) -> {
            //全局设置主题颜色（优先级第二低，可以覆盖 DefaultRefreshInitializer 的配置，与下面的ClassicsHeader绑定）
            layout.setEnableHeaderTranslationContent(true);
            return new ClassicsHeader(context).setTimeFormat(new DynamicTimeFormat("更新于 %s"));
        });
        //刷新脚部
        SmartRefreshLayout.setDefaultRefreshFooterCreator((context, layout) -> new ClassicsFooter(context));
    }
}
