package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.LoginMvp;
import com.example.school_bus.NetWork.MyApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class LoginPresenter implements LoginMvp.presenter{

    private LoginMvp.view view;

    public LoginPresenter(LoginMvp.view view) {
        this.view = view;
    }

    @Override
    public void login(String username, String password, String phone, int type) {
        Observable<UserData> newsDataObservable = MyApi.createApi().login(username, password, phone, type);
        newsDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserData userData) {
                        view.loginResult(userData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
