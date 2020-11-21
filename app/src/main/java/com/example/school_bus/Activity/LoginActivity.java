package com.example.school_bus.Activity;

import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.example.school_bus.Entity.UserData;
import com.example.school_bus.Mvp.LoginAMvp;
import com.example.school_bus.NetWork.API_login;
import com.example.school_bus.Presenter.LoginPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.CustomizeUtils;
import com.example.school_bus.Utils.MyLog;
import com.mob.MobSDK;
import com.mob.secverify.CustomUIRegister;
import com.mob.secverify.GetTokenCallback;
import com.mob.secverify.PageCallback;
import com.mob.secverify.PreVerifyCallback;
import com.mob.secverify.SecVerify;
import com.mob.secverify.datatype.VerifyResult;
import com.mob.secverify.exception.VerifyException;
import com.mob.secverify.ui.component.CommonProgressDialog;
import com.mob.tools.utils.SharePrefrenceHelper;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2020-11-21 11:09
 * @类名 LoginActivity
 * @所在包 com\example\school_bus\Activity\LoginActivity.java
 */
public class LoginActivity extends BaseActivity implements LoginAMvp.view {

    private static String TAG = "LoginActivity";
    @BindView(R.id.login_account)
    EditText loginAccount;
    @BindView(R.id.login_password)
    EditText loginPassword;
    @BindView(R.id.iv_background)
    ImageView ivBackground;
    @BindView(R.id.btn_phone)
    Button btnPhone;
    @BindView(R.id.btn_login)
    Button btnLogin;
    @BindView(R.id.btn_register)
    Button btnRegister;
    private LoginPresenter loginPresenter;
    private SharePrefrenceHelper sharePrefrenceHelper;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        ButterKnife.bind(this);
        checkPermissions();//检查权限
        preLogin();//预登录
        initView();
        initData();
    }

    public void initView() {
        ButterKnife.bind(this);
    }

    public void initData() {
        loginPresenter = new LoginPresenter(this);
        //查找是否有存储的账号密码
        SharedPreferences sharedPreferences = getSharedPreferences("user", MODE_PRIVATE);
        loginAccount.setText(sharedPreferences.getString("username", ""));
        loginPassword.setText(sharedPreferences.getString("password", ""));
    }

    /**
     * 预登录
     */
    private void preLogin(){
        SecVerify.preVerify(new PreVerifyCallback() {
            @Override
            public void onComplete(Void data) {
                Toast.makeText(LoginActivity.this, "预登录成功", Toast.LENGTH_SHORT).show();
            }

            @Override
            public void onFailure(VerifyException e) {
                Toast.makeText(LoginActivity.this, "预登录失败", Toast.LENGTH_SHORT).show();
                Throwable t = e.getCause();
                String errDetail = null;
                if (t != null) {
                    errDetail = t.getMessage();
                }
                // 错误码
                int errCode = e.getCode();
                // 错误信息
                String errMsg = e.getMessage();
                // 更详细的网络错误信息可以通过t查看，请注意：t有可能为null
                String msg = "错误码: " + errCode + "\n错误信息: " + errMsg;
                if (!TextUtils.isEmpty(errDetail)) {
                    msg += "\n详细信息: " + errDetail;
                }
                MyLog.e(TAG, msg);
            }
        });
    }

    /* 检查使用权限 */
    private void checkPermissions() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            try {
                PackageManager pm = getPackageManager();
                PackageInfo pi = pm.getPackageInfo(getPackageName(), PackageManager.GET_PERMISSIONS);
                ArrayList<String> list = new ArrayList<String>();
                for (String p : pi.requestedPermissions) {
                    if (checkSelfPermission(p) != PackageManager.PERMISSION_GRANTED) {
                        list.add(p);
                    }
                }
                if (list.size() > 0) {
                    String[] permissions = list.toArray(new String[list.size()]);
                    if (permissions != null) {
                        requestPermissions(permissions, 1);
                    }
                }
            } catch (Throwable t) {
                t.printStackTrace();
            }
        }
    }

    /**
     * 构建秒验一键登录弹窗
     */
    private void customizeUi() {
        SecVerify.setUiSettings(CustomizeUtils.customizeUi4());
        SecVerify.setLandUiSettings(CustomizeUtils.customizeLandUi4());
    }

    /**
     * 将秒验一键登录弹窗显示
     */
    private void addCustomView() {
        CustomUIRegister.addCustomizedUi(CustomizeUtils.buildCustomView4(MobSDK.getContext()), null);
        CustomUIRegister.addTitleBarCustomizedUi(null,null);
    }

    @OnClick({R.id.btn_phone, R.id.btn_login, R.id.btn_register})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.btn_login:
                //账号密码登录
                Observable<UserData> newsDataObservable = API_login.createApi().login(loginAccount.getText().toString(), loginPassword.getText().toString(), null, 0);
                newsDataObservable
                        .subscribeOn(Schedulers.io())
                        .observeOn(AndroidSchedulers.mainThread())
                        .subscribe(new Observer<UserData>() {
                            @Override
                            public void onSubscribe(Disposable d) {

                            }

                            @Override
                            public void onNext(UserData userData) {
                                //存储输入的账号密码
                                SharedPreferences.Editor editor = getSharedPreferences("user", MODE_PRIVATE).edit();
                                editor.putString("username", userData.getData().getUsername());
                                editor.putString("password", userData.getData().getPassword());
                                editor.putString("phone", userData.getData().getPhone());
                                editor.putString("token", userData.getData().getToken());
                                editor.apply();
                                Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                                startActivity(intent);
                            }

                            @Override
                            public void onError(Throwable e) {

                            }

                            @Override
                            public void onComplete() {

                            }
                        });
                break;
            case R.id.btn_phone:
                //手机一键登录
                addCustomView();
                customizeUi();
                SecVerify.autoFinishOAuthPage(false);//关闭“自动关闭”等待框
                CommonProgressDialog.showProgressDialog(this);
                SecVerify.verify(new PageCallback() {
                    @Override
                    public void pageCallback(int code, String desc) {
                        MyLog.e(TAG, code + " " + desc);
                        if (code != 6119140) {
                            CommonProgressDialog.dismissProgressDialog();
                            Toast.makeText(LoginActivity.this, code + " " + desc, Toast.LENGTH_SHORT).show();
                        }
                    }
                }, new GetTokenCallback() {
                    @Override
                    public void onComplete(VerifyResult data) {
                        //这里是登录成功的位置，在此处调用服务器登录接口
                        CommonProgressDialog.dismissProgressDialog();
                        MyLog.e(TAG, "登录成功");
                        MyLog.e(TAG, "operator:" + data.getOperator());
                        MyLog.e(TAG, "opToken:" + data.getOpToken());
                        MyLog.e(TAG, "token:" + data.getToken());
                        SecVerify.finishOAuthPage();//关闭登录一键弹窗
                        Intent intent = new Intent(LoginActivity.this, MapActivity.class);
                        startActivity(intent);
                    }

                    @Override
                    public void onFailure(VerifyException e) {

                    }
                });
                break;
            case R.id.btn_register:
                break;
        }
    }

    @Override
    public void onComplete(String type) {

    }

    @Override
    public void onError(Throwable e, String type) {

    }
}
