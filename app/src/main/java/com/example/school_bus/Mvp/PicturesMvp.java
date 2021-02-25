package com.example.school_bus.Mvp;

import android.content.Context;
import android.graphics.drawable.Drawable;

import com.example.school_bus.Entity.PicturesData;

import java.util.List;

public interface PicturesMvp {
    interface view{
        void getPicturesResult2(List<Drawable> list);
    }

    interface presenter{
        void getPictures2(int count, Context context);
    }
}
