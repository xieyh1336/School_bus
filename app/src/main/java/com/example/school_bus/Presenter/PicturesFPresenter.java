package com.example.school_bus.Presenter;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.Drawable;
import android.os.Handler;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.DataSource;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.engine.GlideException;
import com.bumptech.glide.request.RequestListener;
import com.bumptech.glide.request.target.CustomTarget;
import com.bumptech.glide.request.target.Target;
import com.bumptech.glide.request.transition.Transition;
import com.example.school_bus.Mvp.PicturesFMvp;
import com.example.school_bus.Utils.MyLog;

import java.util.ArrayList;
import java.util.List;

public class PicturesFPresenter implements PicturesFMvp.presenter {

    private PicturesFMvp.view view;
    private int i = 0;
    private boolean finishLoading = true;
    public PicturesFPresenter(PicturesFMvp.view view) {
        this.view = view;
    }

//    @Override
//    public void getPictures(String page, String count, boolean isLoadMore) {
//        Observable<PicturesData> newsDataObservable = API_1.createApi().getPictures(page, count);
//        newsDataObservable
//                .subscribeOn(Schedulers.io())
//                .observeOn(AndroidSchedulers.mainThread())
//                .subscribe(new Observer<PicturesData>() {
//                    @Override
//                    public void onSubscribe(Disposable d) {
//
//                    }
//
//                    @Override
//                    public void onNext(PicturesData picturesData) {
//                        view.getPicturesResult(picturesData, isLoadMore);
//                    }
//
//                    @Override
//                    public void onError(Throwable e) {
//                        view.onError(e,"0");
//                    }
//
//                    @Override
//                    public void onComplete() {
//                        view.onComplete("0");
//                    }
//                });
//    }

    @Override
    public void getPictures2(int count, boolean isLoadMore, Context context) {
        Handler handler = new Handler();
        List<Drawable> list = new ArrayList<>();
        i = 0;
        finishLoading = true;
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                //while循环是保证每次加载完之后才继续加载，防止图片重复
                while (finishLoading){
                    finishLoading = false;
                    i++;
                    if (i > count){
                        handler.removeCallbacksAndMessages(null);
                        MyLog.e("picture", "Runnable结束，handler已被remove， 当前list长度为" + list.size());
                        view.getPicturesResult2(list, isLoadMore);
                    }else {
                        String Url;
                        Url = "https://api.ixiaowai.cn/api/api.php";
                        MyLog.e("picture", "1、图片Url：" + Url);
                        MyLog.e("picture", "2、正在加载第" + i + "张图片");
                        MyLog.e("picture", "3、当前的isLoadMore为：" + isLoadMore);
                        MyLog.e("picture", "4、当前list长度为" + list.size());
                        if (context == null || ((Activity) context).isFinishing()){
                            MyLog.e("picture", "Activity已销毁");
                            return;
                        }
                        Glide
                                .with(context)
                                .load(Url)
                                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭磁盘缓存
                                .skipMemoryCache(true)//跳过内存缓存
                                .listener(new RequestListener<Drawable>() {//监听
                                    @Override
                                    public boolean onLoadFailed(@Nullable GlideException e, Object model, Target<Drawable> target, boolean isFirstResource) {
                                        MyLog.e("picture", "onException");
                                        finishLoading = true;
                                        return false;
                                    }

                                    @Override
                                    public boolean onResourceReady(Drawable resource, Object model, Target<Drawable> target, DataSource dataSource, boolean isFirstResource) {
                                        MyLog.e("picture", "onResourceReady");
                                        finishLoading = true;
                                        return false;
                                    }
                                })
                                .into(new CustomTarget<Drawable>() {
                                    @Override
                                    public void onResourceReady(@NonNull Drawable resource, @Nullable Transition<? super Drawable> transition) {
                                        finishLoading = true;
                                        MyLog.e("picture", "Glide加载完毕，现在在第" + i + "项");
                                        list.add(resource);
                                    }

                                    @Override
                                    public void onLoadCleared(@Nullable Drawable placeholder) {
                                        finishLoading = true;
                                    }
                                });
                    }
                }
                handler.post(this);
            }
        };
        handler.post(runnable);
    }
}
