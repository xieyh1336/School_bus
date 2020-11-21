package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.MainMvp;
import com.example.school_bus.NetWork.API_login;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MainPresenter implements MainMvp.presenter {

    private MainMvp.view view;

    public MainPresenter(MainMvp.view view) {
        this.view = view;
    }

    @Override
    public void tokenLogin(String token) {
        Observable<UserData> newsDataObservable = API_login.createApi().tokenLogin(token);
        newsDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserData userData) {
                        view.tokenLoginResult(userData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "tokenLogin");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
