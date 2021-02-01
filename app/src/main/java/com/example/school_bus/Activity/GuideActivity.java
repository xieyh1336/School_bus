package com.example.school_bus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.MainMvp;
import com.example.school_bus.Presenter.MainPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;
import com.example.school_bus.Utils.ImageUtil;
import com.example.school_bus.Utils.MyLog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 17:02
 * @类名 GuideActivity
 * @所在包 com\example\school_bus\Activity\GuideActivity.java
 * app引导页
 */
public class GuideActivity extends BaseActivity implements MainMvp.view {
    private static String TAG = "GuideActivity";
    @BindView(R.id.iv_loading)
    ImageView ivLoading;
    private MainPresenter mainPresenter = new MainPresenter(this);

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_guide);
        ButterKnife.bind(this);
        init();
        new Handler().postDelayed(this::login, 2000);
    }

    private void init() {
        Animation animation = AnimationUtils.loadAnimation(this, R.anim.to_big);
        animation.setFillAfter(true);
        ivLoading.startAnimation(animation);
        Glide.with(this)
                .load(ImageUtil.getHeadUrl("main.png"))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .into(ivLoading);
    }

    private void login() {
        //尝试使用token自动登录
        mainPresenter.tokenLogin();
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
            startActivity(new Intent(GuideActivity.this, MainActivity.class));
        } else {
            //跳转登陆页面
            MyLog.e(TAG, "token已过期，跳转登录页面");
            startActivity(new Intent(GuideActivity.this, LoginActivity.class));
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