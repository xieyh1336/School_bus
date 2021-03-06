package com.example.school_bus.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.CompoundButton;
import android.widget.PopupWindow;

import androidx.appcompat.widget.SwitchCompat;

import com.example.school_bus.R;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-23 11:51
 * @类名 MyPopupWindow
 * @所在包 com\example\school_bus\View\MyPopupWindow.java
 * 弹窗
 */
public class MyPopupWindow implements View.OnClickListener, SwitchCompat.OnCheckedChangeListener {

    private Context context;
    private View view;
    private PopupWindow popupWindow;
    private OnClickListener onClickListener;//点击监听
    private OnSwitchListener onSwitchListener;//选择框监听
    private int height, width;//记录popup的宽高

    public final static String TAKE_PHOTO = "take_photo";//拍照
    public final static String SELECT_PHOTO = "select_photo";//选择相册
    public final static String SAVE_PICTURE = "save_picture";//保存图片

    public MyPopupWindow(Context context) {
        this.context = context;
    }

    /*--------------------------------------------以下是通用功能--------------------------------------------*/

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(String type);
    }

    public void setOnSwitchListener(OnSwitchListener onSwitchListener) {
        this.onSwitchListener = onSwitchListener;
    }

    public interface OnSwitchListener{
        void onSwitch(CompoundButton buttonView, int num, boolean isSelect);
    }

    public void hide(){
        if (popupWindow != null && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }

    public PopupWindow getPopup(){
        return popupWindow;
    }

    /*--------------------------------------------以下是定制区域--------------------------------------------*/

    /**
     * 相册弹框
     */
    @SuppressLint("InflateParams")
    public void showPhoto(){
        if (popupWindow == null){
            view = LayoutInflater.from(context).inflate(R.layout.popup_photo, null);
            popupWindow = new PopupWindow(view, -1, -2, true);
            popupWindow.setAnimationStyle(R.style.popup_window_bottom_style);//下方进入的动画
            popupWindow.setOutsideTouchable(true);//区域外可点击
            view.findViewById(R.id.tv_take_photo).setOnClickListener(this);
            view.findViewById(R.id.tv_select_photo).setOnClickListener(this);
            view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        }
        if (!popupWindow.isShowing()){
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);//底部显示
        }
    }

    /**
     * 保存图片弹框
     */
    @SuppressLint("InflateParams")
    public void showPicture(){
        if (popupWindow == null){
            view = LayoutInflater.from(context).inflate(R.layout.popup_picture, null);
            popupWindow = new PopupWindow(view, -1, -2, true);
            popupWindow.setAnimationStyle(R.style.popup_window_bottom_style);//下方进入动画
            popupWindow.setOutsideTouchable(true);//区域外可点击
            view.findViewById(R.id.tv_save).setOnClickListener(this);
            view.findViewById(R.id.tv_cancel).setOnClickListener(this);
        }
        if (!popupWindow.isShowing()){
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);//底部显示
        }
    }

    /**
     * 地图页功能弹框
     * @param parent 依赖的view
     */
    @SuppressLint("InflateParams")
    public void showMapFunction(View parent){
        if (popupWindow == null){
            view = LayoutInflater.from(context).inflate(R.layout.popup_map_function, null);
            view.measure(View.MeasureSpec.UNSPECIFIED, View.MeasureSpec.UNSPECIFIED);
            height = view.getMeasuredHeight();
            width = view.getMeasuredWidth();
            popupWindow = new PopupWindow(view, -2, -2, true);
            popupWindow.setOutsideTouchable(true);//区域外可点击

            ((SwitchCompat) view.findViewById(R.id.switch1)).setOnCheckedChangeListener(this);
            ((SwitchCompat) view.findViewById(R.id.switch2)).setOnCheckedChangeListener(this);
            ((SwitchCompat) view.findViewById(R.id.switch3)).setOnCheckedChangeListener(this);
        }
        if (!popupWindow.isShowing()){
            popupWindow.showAsDropDown(parent, 0, -(height + parent.getMeasuredHeight()));//显示在上方
        }
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_take_photo:
                //拍照
                if (onClickListener != null){
                    onClickListener.onClick(TAKE_PHOTO);
                }
                hide();
                break;
            case R.id.tv_select_photo:
                //选择相册
                if (onClickListener != null){
                    onClickListener.onClick(SELECT_PHOTO);
                }
                hide();
                break;
            case R.id.tv_save:
                //保存图片
                if (onClickListener != null){
                    onClickListener.onClick(SAVE_PICTURE);
                }
                hide();
                break;
            case R.id.tv_cancel:
                //取消键
                hide();
                break;
        }
    }

    @Override
    public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
        switch (buttonView.getId()){
            case R.id.switch1:
                //第一个switch
                if (onSwitchListener != null){
                    onSwitchListener.onSwitch(buttonView, 1, isChecked);
                }
                break;
            case R.id.switch2:
                //第二个switch
                if (onSwitchListener != null){
                    onSwitchListener.onSwitch(buttonView, 2, isChecked);
                }
                break;
            case R.id.switch3:
                //第三个switch
                if (onSwitchListener != null){
                    onSwitchListener.onSwitch(buttonView, 3, isChecked);
                }
                break;
        }
    }
}
