package com.example.school_bus.Activity;

import android.app.Dialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import com.example.school_bus.R;

import java.util.Objects;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-04 15:07
 * @类名 BaseActivity
 * @所在包 com\example\school_bus\Activity\BaseActivity.java
 * Activity基类
 */
public abstract class BaseActivity extends AppCompatActivity {
    //以下是关闭app的广播字段
    public final static String RECEIVER_ACTION_FINISH_MAIN = "receiver_action_finish_main";
    public final static String RECEIVER_ACTION_FINISH_OFFLINE_MAP = "receiver_action_finish_offline_map";
    private FinishActivityReceiver finishActivityReceiver;

    //以下是loading和toast
    protected Dialog loadingDialog;

    public void showToast(String msg){
        Toast.makeText(getApplicationContext(), msg, Toast.LENGTH_SHORT).show();
    }

    public void startLogin(){
        startActivity(new Intent(this, LoginActivity.class));
    }

    /**
     *将显示Dialog的方法封装在这里面
     */
    public void showLoading(String msg) {
        LayoutInflater inflater = LayoutInflater.from(this);
        View v = inflater.inflate(R.layout.dialog_loading, null); // 得到加载view
        LinearLayout layout = v.findViewById(R.id.dialog_loading_view); // 加载布局
        TextView tipTextView = v.findViewById(R.id.tipTextView); // 提示文字
        tipTextView.setText(msg); // 设置加载信息
        loadingDialog = new Dialog(this, R.style.MyDialogStyle); // 创建自定义样式dialog
        loadingDialog.setCancelable(true); // 是否可以按“返回键”消失
        loadingDialog.setCanceledOnTouchOutside(false); // 点击加载框以外的区域
        loadingDialog.setContentView(layout, new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT)); // 设置布局
        Window window = loadingDialog.getWindow();
        WindowManager.LayoutParams lp = Objects.requireNonNull(window).getAttributes();
        lp.width = WindowManager.LayoutParams.MATCH_PARENT;
        lp.height = WindowManager.LayoutParams.WRAP_CONTENT;
        window.setGravity(Gravity.CENTER);
        window.setAttributes(lp);
        window.setWindowAnimations(R.style.PopWindowAnimStyle);
        loadingDialog.show();
    }

    /**
     * 关闭dialog
     */
    public void hideLoading() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            try {
                loadingDialog.dismiss();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //关闭activity广播
        finishActivityReceiver = new FinishActivityReceiver();
        IntentFilter intentFilter = new IntentFilter();
        intentFilter.addAction(RECEIVER_ACTION_FINISH_MAIN);
        intentFilter.addAction(RECEIVER_ACTION_FINISH_OFFLINE_MAP);
        registerReceiver(finishActivityReceiver, intentFilter);
    }

    @Override
    protected void onDestroy() {
        if (finishActivityReceiver != null){
            unregisterReceiver(finishActivityReceiver);
        }
        super.onDestroy();
    }

    /**
     * 结束activity广播类
     */
    private class FinishActivityReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            if (RECEIVER_ACTION_FINISH_MAIN.equals(intent.getAction()) ||
                    RECEIVER_ACTION_FINISH_OFFLINE_MAP.equals(intent.getAction())){
                BaseActivity.this.finish();
            }
        }
    }
}
