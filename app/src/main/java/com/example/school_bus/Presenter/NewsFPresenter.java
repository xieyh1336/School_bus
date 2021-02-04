package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Mvp.NewsFMvp;
import com.example.school_bus.NetWork.API_1;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class NewsFPresenter implements NewsFMvp.presenter {

    public NewsFMvp.view view;

    public NewsFPresenter(NewsFMvp.view view) {
        this.view = view;
    }

    @Override
    public void getNews(String page, String count, boolean isLoadMore) {
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
                        if (page.equals("1")){
                            view.getNewsResult(newsData);
                        }else {
                            view.getNewsResult2(newsData, isLoadMore);
                        }
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e,"getNews");
                    }

                    @Override
                    public void onComplete() {
                        view.onComplete("getNews");
                    }
                });
    }
}
