package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.NewsData;

import java.util.List;

public interface MoreFMvp {
    interface view{
        void getNewsResult(NewsData data);
        void onError(Throwable e, int i);
        void onComplete(int i);
    }

    interface presenter{
        void getNews(String page, String count);
    }
}
