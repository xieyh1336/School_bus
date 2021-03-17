package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.BusListData;
import com.example.school_bus.Mvp.OrderMvp;
import com.example.school_bus.NetWork.MyApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class OrderPresenter implements OrderMvp.presenter {

    private OrderMvp.view view;

    public OrderPresenter(OrderMvp.view view) {
        this.view = view;
    }

    @Override
    public void getAllWaitingBus() {
        Observable<BusListData> observable = MyApi.createApi().getAllWaitingBus();
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BusListData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BusListData busListData) {
                        view.getAllWaitingBusResult(busListData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "getAllWaitingBus");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }

    @Override
    public void orderBus(String id) {
        Observable<BaseData> observable = MyApi.createApi().orderBus(id);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<BaseData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(BaseData baseData) {
                        view.orderBusResult(baseData);
                    }

                    @Override
                    public void onError(Throwable e) {
                        view.onError(e, "orderBus");
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
