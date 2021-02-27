package com.example.school_bus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.LoginMvp;
import com.example.school_bus.Presenter.LoginPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2020-11-21 11:09
 * @类名 LoginActivity
 * @所在包 com\example\school_bus\Activity\LoginActivity.java
 * 登录页面
 */
public class LoginActivity extends BaseActivity implements LoginMvp.view {

    private static String TAG = "LoginActivity";
    @BindView(R.id.login_account)
    EditText etLoginAccount;
    @BindView(R.id.login_password)
    EditText etLoginPassword;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private long secondBackTime;
    private LoginPresenter loginPresenter = new LoginPresenter(this);

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        init();
    }

    public void init() {
        Glide.with(this).load(R.drawable.lightning_five_whip).into(ivBackground);
        //查找是否有存储的账号密码
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        etLoginAccount.setText(sharedPreferences.getString("username", ""));
        etLoginPassword.setText(sharedPreferences.getString("password", ""));
    }

    @Override
    public void onBackPressed() {
        if (System.currentTimeMillis() - secondBackTime > 2000){
            showToast("再按一次退出");
            secondBackTime = System.currentTimeMillis();
        }else {
            //退出
            finish();
        }
    }

    @OnClick({R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //账号密码登录
                showLoading("正在登录...");
                loginPresenter.login(etLoginAccount.getText().toString(), etLoginPassword.getText().toString(), null, 0);
                break;
            case R.id.btn_register:
                //注册
                startActivityForResult(new Intent(LoginActivity.this, RegisterActivity.class), 200);
                break;
        }
    }

    @Override
    public void loginResult(UserData userData) {
        if (userData.isSuccess()){
            //存储输入的账号密码
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("username", userData.getData().getUsername());
            editor.putString("password", userData.getData().getPassword());
            editor.putString("phone", userData.getData().getPhone());
            editor.putString("token", userData.getData().getToken());
            editor.putString("head", userData.getData().getHead());
            editor.apply();
            hideLoading();
            startActivity(new Intent(LoginActivity.this, MainActivity.class));
            finish();
        }else {
            showToast(userData.getMessage());
        }
    }

    @Override
    public void onError(Throwable e) {
        HttpUtil.onError(e);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 200 && resultCode == 400){
            //注册成功
            SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
            String username = sharedPreferences.getString("username", "");
            String password = sharedPreferences.getString("password", "");
            etLoginAccount.setText(username);
            etLoginPassword.setText(password);
        }
    }
}
