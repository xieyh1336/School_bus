package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.RegisterMvp;
import com.example.school_bus.NetWork.API_login;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterPresenter implements RegisterMvp.presenter {

    private RegisterMvp.view view;

    public RegisterPresenter(RegisterMvp.view view) {
        this.view = view;
    }

    @Override
    public void register(String username, String password, String phone) {
        Observable<UserData> newsDataObservable = API_login.createApi().register(username, password, phone);
        newsDataObservable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserData userData) {
                        view.registerResult(userData);
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
