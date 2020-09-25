package com.example.school_bus.Utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.example.school_bus.BuildConfig;
import com.example.school_bus.R;


/**
 * Created by Administrator on 2017/8/24.
 */

public class MyToast {
    private static Toast mToast = null;
    private static Context mContext;

    public static void init(Context context) {
        mToast = new Toast(context);
        mContext = context;
    }
    public static Context getContent(){
        return mContext;
    }

    public static void ShowToast(CharSequence msg) {

        View toastView = LayoutInflater.from(mContext).inflate(R.layout.toast_shou, null);
        TextView textView = (TextView) toastView.findViewById(R.id.tv_toast_shou);
            if (!TextUtils.isEmpty(msg)) {
                if (mToast == null){
                    mToast = new Toast(mContext);
                }
                mToast.setView(toastView);
                textView.setText(msg);
                mToast.show();
            }
    }

    public static void ShowToast(CharSequence msg, int gravity, int xOffset, int yOffset) {

        View toastView = LayoutInflater.from(mContext).inflate(R.layout.toast_shou, null);
        TextView textView = (TextView) toastView.findViewById(R.id.tv_toast_shou);
        if (!TextUtils.isEmpty(msg)) {
            if (mToast == null){
                mToast = new Toast(mContext);
            }
            mToast.setView(toastView);
            mToast.setGravity(gravity, xOffset, yOffset);
            textView.setText(msg);
            mToast.show();
        }
    }

    public static void DebugToast(CharSequence msg) {

        View toastView = LayoutInflater.from(mContext).inflate(R.layout.toast_shou, null);

        TextView textView = (TextView) toastView.findViewById(R.id.tv_toast_shou);

    }

    /**
     * 返回版本名字
     * 对应build.gradle中的versionName
     *
     * @return
     */
    public static String getVersionName() {
        String versionName = null;
        try {
            PackageManager packageManager = mContext.getPackageManager();
            PackageInfo packInfo = packageManager.getPackageInfo(mContext.getPackageName(), 0);
            versionName = packInfo.versionName;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return versionName;
    }
}
