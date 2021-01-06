package com.example.school_bus.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.NetWork.API_login;
import com.example.school_bus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class RegisterActivity extends BaseActivity {

    @BindView(R.id.et_login_account)
    EditText etLoginAccount;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_register);
        ButterKnife.bind(this);
    }

    @OnClick({R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_register:
                Observable<UserData> newsDataObservable = API_login.createApi().register(etLoginAccount.getText().toString(), etLoginPassword.getText().toString(), etLoginPhone.getText().toString());
                newsDataObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UserData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(UserData userData) {
                                if (userData.getCode() == 20000){
                                    showToast("注册成功");
                                    SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                    editor.putString("username", userData.getData().getUsername());
                                    editor.putString("password", userData.getData().getPassword());
                                    editor.putString("phone", userData.getData().getPhone());
                                    editor.apply();
                                    setResult(400);
                                    finish();
                                }else {
                                    showToast(userData.getMessage());
                                }
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
        }
    }
}