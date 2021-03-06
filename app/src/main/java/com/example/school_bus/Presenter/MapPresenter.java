package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.TestBus;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.MapMvp;
import com.example.school_bus.NetWork.MyApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class MapPresenter implements MapMvp.presenter {

    private MapMvp.view view;

    public MapPresenter(MapMvp.view view) {
        this.view = view;
    }

    @Override
    public void upLocation(double latitude, double longitude) {
        Observable<UserData> observable = MyApi.createApi().upLocation(latitude, longitude);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserData userData) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void testInsertBus(double latitude, double longitude) {
        Observable<UserData> observable = MyApi.createApi().testInsertBus(latitude, longitude);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserData userData) {

                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void testGetBus() {
        Observable<TestBus> observable = MyApi.createApi().testGetBus();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<TestBus>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(TestBus testBus) {
                        view.testGetBusResult(testBus);
                    }

                    @Override
                    public void onError(Throwable e) {

                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
