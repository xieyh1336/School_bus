package com.example.school_bus.Mvp;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.school_bus.Entity.PicturesData;

import java.util.List;

public interface PicturesFMvp {
    interface view{
//        void getPicturesResult(PicturesData data, boolean isLoadMore);
        void getPicturesResult2(List<Drawable> list, boolean isLoadMore);
        void onError(Throwable e, String type);
        void onComplete(String type);
    }

    interface presenter{
//        void getPictures(String page, String count, boolean isLoadMore);
        void getPictures2(int count, boolean isLoadMore, Context context);
    }
}
