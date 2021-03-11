package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.DriverStateData;
import com.example.school_bus.Mvp.DriverMvp;
import com.example.school_bus.NetWork.MyApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class DriverPresenter implements DriverMvp.presenter {

    private DriverMvp.view view;

    public DriverPresenter(DriverMvp.view view) {
        this.view = view;
    }

    @Override
    public void runBus(String id, double latitude, double longitude) {
        Observable<BaseData> observable = MyApi.createApi().runBus(id, latitude, longitude);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseData baseData) {
                        view.runBusResult(baseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "runBus");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getDriverState() {
        Observable<DriverStateData> observable = MyApi.createApi().getDriverState();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<DriverStateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(DriverStateData driverStateData) {
                        view.getDriverStateResult(driverStateData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "getDriverState");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void arriveBus(double latitude, double longitude) {
        Observable<BaseData> observable = MyApi.createApi().arriveBus(latitude, longitude);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseData baseData) {
                        view.arriveBusResult(baseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "arriveBus");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void upBusLocation(double latitude, double longitude) {
        Observable<BaseData> observable = MyApi.createApi().upBusLocation(latitude, longitude);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseData baseData) {
                        view.upBusLocationResult(baseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "arriveBus");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
