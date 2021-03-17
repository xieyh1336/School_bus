package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.AllOrderData;
import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.MyStateData;
import com.example.school_bus.Mvp.MyOrderMvp;
import com.example.school_bus.NetWork.MyApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;


public class MyOrderPresenter implements MyOrderMvp.presenter {

    private MyOrderMvp.view view;

    public MyOrderPresenter(MyOrderMvp.view view) {
        this.view = view;
    }

    @Override
    public void getMyState() {
        Observable<MyStateData> observable = MyApi.createApi().getMyState();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<MyStateData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(MyStateData myStateData) {
                        view.getMyStateResult(myStateData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "getMyState");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void getAllOrder() {
        Observable<AllOrderData> observable = MyApi.createApi().getAllOrder();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<AllOrderData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(AllOrderData allOrderData) {
                        view.getAllOrderResult(allOrderData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "getAllOrder");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void cancelOrder() {
        Observable<BaseData> observable = MyApi.createApi().cancelOrder();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseData baseData) {
                        view.cancelOrderResult(baseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "cancelOrder");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
