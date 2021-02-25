package com.example.school_bus.Activity;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.RegisterMvp;
import com.example.school_bus.Presenter.RegisterPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 17:01
 * @类名 RegisterActivity
 * @所在包 com\example\school_bus\Activity\RegisterActivity.java
 * 注册页面
 */
public class RegisterActivity extends BaseActivity implements RegisterMvp.view {

    @BindView(R.id.et_login_account)
    EditText etLoginAccount;
    @BindView(R.id.et_login_phone)
    EditText etLoginPhone;
    @BindView(R.id.et_login_password)
    EditText etLoginPassword;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private RegisterPresenter registerPresenter = new RegisterPresenter(this);

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
                registerPresenter.register(etLoginAccount.getText().toString(), etLoginPassword.getText().toString(), etLoginPhone.getText().toString());
                break;
        }
    }

    @Override
    public void registerResult(UserData userData) {
        if (userData.isSuccess()){
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
}