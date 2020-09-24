package com.example.school_bus.View;

import android.content.Context;
import android.util.Log;
import android.view.View;

import androidx.annotation.NonNull;

import com.example.school_bus.R;
import com.lxj.xpopup.core.DrawerPopupView;



/**
 * 点击菜单出现的侧边栏
 */
public class PagerDrawerPopupView extends DrawerPopupView {

    private DataResult dataResult;

    public PagerDrawerPopupView(@NonNull Context context) {
        super(context);
    }

    @Override
    protected int getImplLayoutId() {
        return R.layout.view_custom_pager_drawer;
    }

    @Override
    protected void onCreate() {
        super.onCreate();
        SwitchView switch1 = findViewById(R.id.switch1);
        switch1.toggleSwitch(false);
        switch1.setColor(0xFF1878FF,0xFFFFFFFF);
        switch1.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                dataResult.isShowLocation(true);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                dataResult.isShowLocation(false);
            }
        });
    }

    public interface DataResult{
        void isShowLocation(boolean show);
    }

    public void setDataResult(DataResult dataResult) {
        this.dataResult = dataResult;
    }

    @Override
    protected void onShow() {
        super.onShow();
        Log.e("tag", "PagerDrawerPopup onShow");
    }

    @Override
    protected void onDismiss() {
        super.onDismiss();
        Log.e("tag", "PagerDrawerPopup onDismiss");
    }
}
