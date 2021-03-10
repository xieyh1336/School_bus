package com.example.school_bus.Presenter;

import com.example.school_bus.Entity.BusListData;
import com.example.school_bus.Mvp.WaitingBusListMvp;
import com.example.school_bus.NetWork.MyApi;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class WaitingBusListPresenter implements WaitingBusListMvp.presenter {

    private WaitingBusListMvp.view view;

    public WaitingBusListPresenter(WaitingBusListMvp.view view) {
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
                        view.onError(e);
                    }

                    @Override
                    public void onComplete() {

                    }
                });
    }
}
