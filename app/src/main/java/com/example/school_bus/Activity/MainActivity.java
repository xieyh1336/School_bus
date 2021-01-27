package com.example.school_bus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.MainMvp;
import com.example.school_bus.Presenter.MainPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;
import com.example.school_bus.Utils.MyLog;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 17:02
 * @类名 MainActivity
 * @所在包 com\example\school_bus\Activity\MainActivity.java
 * 自动登录页面
 */
public class MainActivity extends BaseActivity implements MainMvp.view {
    private static String TAG = "MainActivity";
    @BindView(R.id.skv)
    SpinKitView skv;
    private MainPresenter mainPresenter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        initView();
        login();
    }

    private void initView(){
        mainPresenter = new MainPresenter(this);
        ButterKnife.bind(this);
        Style style = Style.values()[1];
        Sprite drawable = SpriteFactory.create(style);
        skv.setIndeterminateDrawable(drawable);
    }

    private void login() {
        //查找token是否存在
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        String token = sharedPreferences.getString("token", null);
        MyLog.e(TAG, "准备自动登录，查询是否存储有token...");
        MyLog.e(TAG, "存储的token:" + token);
        //尝试使用token自动登录
        if (token == null){
            startLogin();
            finish();
        }else {
            mainPresenter.tokenLogin();
        }
    }

    @Override
    public void tokenLoginResult(UserData userData) {
        if (userData.isSuccess()) {
            MyLog.e(TAG, "token自动登录成功");
            //存储登录人的基本信息
            SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
            editor.putString("username", userData.getData().getUsername());
            editor.putString("password", userData.getData().getPassword());
            editor.putString("phone", userData.getData().getPhone());
            editor.putString("token", userData.getData().getToken());
            editor.putString("head", userData.getData().getHead());
            editor.apply();
            startActivity(new Intent(MainActivity.this, MapActivity.class));
        } else {
            //跳转登陆页面
            MyLog.e(TAG, "token已过期，跳转登录页面");
            startActivity(new Intent(MainActivity.this, LoginActivity.class));
        }
        finish();
    }

    @Override
    public void onError(Throwable e, String type) {
        if ("tokenLogin".equals(type)) {//跳转登陆页面
            MyLog.e(TAG, "token自动登录访问出错，跳转登录页面");
            HttpUtil.onError(this, e);
            startLogin();
            finish();
        }
    }
}