package com.example.school_bus.Presenter;

import android.app.Activity;
import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.transition.Transition;
import com.example.school_bus.Mvp.PicturesFMvp;
import com.example.school_bus.Utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class PicturesFPresenter implements PicturesFMvp.presenter {

    private PicturesFMvp.view view;
    private int i = 0;
    private Handler handler;
    private Runnable runnable;
    private List<Drawable> list;
    public PicturesFPresenter(PicturesFMvp.view view) {
        this.view = view;
    }

    @Override
    public void getPictures2(int count, Context context) {
        if (handler == null){
            handler = new Handler();
        }
        if (list == null){
            list = new ArrayList<>();
        } else {
            list.clear();
        }
        initPictureList(count, context);
        handler.post(runnable);
    }

    private void initPictureList(int count, Context context){
        i = 0;
        if (runnable == null) {
            runnable = () -> {
                if (i < count){
                    i += 1;
                    String Url = "https://api.ixiaowai.cn/api/api.php";
                    MyLog.e("picture", "1、图片Url：" + Url);
                    MyLog.e("picture", "2、正在加载第" + i + "张图片");
                    MyLog.e("picture", "3、当前list长度为" + list.size());
                    if (context == null || ((Activity) context).isFinishing()) {
                        MyLog.e("picture", "Activity已销毁");
                        return;
                    }
                    Glide.with(context)
                            .load(Url)
                            .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭磁盘缓存
                            .skipMemoryCache(true)//跳过内存缓存
                            .into(new CustomTarget<Drawable>() {
                                @Override
                                public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                    MyLog.e("picture", "Glide加载完毕，现在在第" + i + "项");
                                    handler.removeCallbacksAndMessages(runnable);
                                    handler.post(runnable);
                                    list.add(resource);
                                }

                                @Override
                                public void onLoadCleared(@Nullable Drawable placeholder) {

                                }

                                @Override
                                public void onLoadFailed(@Nullable Drawable errorDrawable) {
                                    super.onLoadFailed(errorDrawable);
                                    MyLog.e("picture", "Glide加载失败，现在是第" + i + "项");
                                    handler.removeCallbacksAndMessages(runnable);
                                    handler.post(runnable);
                                }
                            });
                } else {
                    MyLog.e("picture", "加载完成，回调");
                    handler.removeCallbacksAndMessages(runnable);
                    view.getPicturesResult2(list);
                }
            };
        }
    }
}
