package com.example.school_bus.Mvp;

import com.example.school_bus.Entity.NewsData;

import java.util.List;

public interface MoreFMvp {
    interface view{
        void getNewsResult(NewsData data);
        void getNewsResult2(NewsData data);
        void onError(Throwable e, int i);
        void onComplete(int i);

        //加载更多列表
        void onInitLoadFailed();
        void stopRefresh();
        void stopLoadMore();
    }

    interface presenter{
        void getNews(String page, String count);

        //加载更多列表
        void onViewCreate();
        void startRefresh();
        void startLoadMore();
    }
}
