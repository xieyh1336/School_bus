package com.example.school_bus.Presenter;

import android.content.SharedPreferences;

import com.example.school_bus.Activity.MainActivity;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.MapSideMvp;
import com.example.school_bus.NetWork.API_login;
import com.example.school_bus.Utils.ImageUtil;
import com.example.school_bus.Utils.MyLog;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;
import okhttp3.MultipartBody;

import static android.content.Context.MODE_PRIVATE;

public class MapSidePresenter implements MapSideMvp.presenter {

    private MapSideMvp.view view;

    public MapSidePresenter(MapSideMvp.view view) {
        this.view = view;
    }

    @Override
    public void upHead(MultipartBody.Part body) {
        Observable<UserData> observable = API_login.createApi().upHead(body);
        observable
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(new Observer<UserData>() {
                    @Override
                    public void onSubscribe(Disposable d) {

                    }

                    @Override
                    public void onNext(UserData userData) {
                        view.upHead(userData);
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
