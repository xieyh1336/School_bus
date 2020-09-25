package com.example.school_bus.Presenter;

import android.util.Log;

import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Mvp.MoreFMvp;
import com.example.school_bus.NetWork.API_1;

import java.util.ArrayList;
import java.util.List;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MoreFPresenter implements MoreFMvp.presenter {

    public MoreFMvp.view view;
    private int mCurrentPage;
    private NewsData data = new NewsData();
    private List<NewsData.ResultBean> list = new ArrayList<>();

    public MoreFPresenter(MoreFMvp.view view) {
        this.view = view;
    }

    @Override
    public void getNews(String page, String count) {
        Observable<NewsData> newsDataObservable = API_1.createApi().getNews(page, count);
        newsDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<NewsData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(NewsData newsData) {
                        if (page == "1"){
                            view.getNewsResult(newsData);
                        }else {
                            // 首次或刷新
                            if (mCurrentPage == 1)
                                list.clear();

                            // 刷新数据
                            list.addAll(newsData.getResult());
                            data.setResult(list);
                            view.getNewsResult2(data);

                            Log.e("adapter", "page:" + mCurrentPage + ", size:" + list.size());
                            // 更新视图
                            if (mCurrentPage == 1) {
                                view.stopRefresh();
                            } else {
                                view.stopLoadMore();
                            }
                            view.getNewsResult2(newsData);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e,0);
                    }

                    @Override
                    public void onComplete() {
                        view.onComplete(0);
                    }
                });
    }

    @Override
    public void onViewCreate() {
        mCurrentPage = 1;
        getNews(String.valueOf(mCurrentPage+1),"7");
    }

    @Override
    public void startRefresh() {
        mCurrentPage = 1;
        getNews(String.valueOf(mCurrentPage+1),"7");
    }

    @Override
    public void startLoadMore() {
        mCurrentPage++;
        getNews(String.valueOf(mCurrentPage+1),"7");
    }
}
