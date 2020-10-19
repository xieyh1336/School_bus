package com.example.school_bus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;

import com.example.school_bus.Mvp.LoginAMvp;
import com.example.school_bus.Presenter.LoginPresenter;
import com.example.school_bus.R;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 登陆页面
 */
public class LoginActivity extends BaseActivity implements LoginAMvp.view {

    @BindView(R.id.login_account)
    EditText loginAccount;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.login_btn)
    Button loginBtn;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    private LoginPresenter loginPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        initView();
        initData();
    }
    public void initView(){
        ButterKnife.bind(this);
    }

    public void initData() {
        loginPresenter = new LoginPresenter(this);
        //查找是否有存储的账号密码
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        loginAccount.setText(sharedPreferences.getString("userName", ""));
        loginPassword.setText(sharedPreferences.getString("userPassword", ""));
    }

    @OnClick(R.id.login_btn)
    public void onViewClicked() {
        //存储输入的账号密码
        SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
        editor.putString("userName", loginAccount.getText().toString());
        editor.putString("userPassword", loginPassword.getText().toString());
        editor.apply();

        if (loginAccount.getText().toString().equals("xyh") && loginPassword.getText().toString().equals("123456")) {
            Intent intent = new Intent(LoginActivity.this, MapActivity.class);
            startActivity(intent);
        } else {
            showToast("账号或密码错误，请重新输入!");
        }
    }

    @Override
    public void onComplete(String type) {

    }

    @Override
    public void onError(Throwable e, String type) {

    }
}
