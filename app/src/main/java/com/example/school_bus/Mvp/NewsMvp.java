package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.NewsData;


public interface NewsMvp {
    interface view{
        void getNewsResult(NewsData data);
        void getNewsResult2(NewsData data, boolean isLoadMore);
        void onError(Throwable e, String type);
        void onComplete(String type);
    }

    interface presenter{
        void getNews(String page, String count, boolean isLoadMore);
    }
}
