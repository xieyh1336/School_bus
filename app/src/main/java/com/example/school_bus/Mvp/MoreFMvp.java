package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.NewsData;


public interface MoreFMvp {
    interface view{
        void getNewsResult(NewsData data);
        void getNewsResult2(NewsData data, boolean isLoadMore);
        void onError(Throwable e, int i);
        void onComplete(int i);
    }

    interface presenter{
        void getNews(String page, String count, boolean isLoadMore);
    }
}
