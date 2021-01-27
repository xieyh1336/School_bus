package com.example.school_bus.View;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.PopupWindow;

import com.example.school_bus.R;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-23 11:51
 * @类名 MyPopupWindow
 * @所在包 com\example\school_bus\View\MyPopupWindow.java
 * 弹窗
 */
public class MyPopupWindow extends PopupWindow implements View.OnClickListener {

    private Context context;
    private View view;
    private PopupWindow popupWindow;
    private boolean isDismissAfterClick = true;//是否点击了任何一项就关闭弹窗
    private OnDismissListener onDismissListener = () -> setBackgroundAlpha(1f);//弹窗消失背景重新设置为正常;
    private OnClickListener onClickListener;//点击监听

    public final static String TAKE_PHOTO = "take_photo";//拍照
    public final static String SELECT_PHOTO = "select_photo";//选择相册

    public MyPopupWindow(Context context) {
        this.context = context;
    }

    /*--------------------------------------------以下是通用功能--------------------------------------------*/

    //设置是否点击任何一项就关闭弹窗
    public void setIsDismissAfterClick(boolean isDismissAfterClick){
        this.isDismissAfterClick = isDismissAfterClick;
    }

    /**
     * 设置背景透明度
     * @param backgroundAlpha 背景透明度
     */
    private void setBackgroundAlpha(float backgroundAlpha){
        WindowManager.LayoutParams layoutParams = ((Activity) context).getWindow().getAttributes();
        layoutParams.alpha = backgroundAlpha;
        ((Activity) context).getWindow().setAttributes(layoutParams);
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(String type);
    }

    /*--------------------------------------------以下是定制区域--------------------------------------------*/

    //相册弹框
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
//            setBackgroundAlpha(0.5f);//背景透明设置一半
            popupWindow.showAtLocation(view, Gravity.BOTTOM, 0, 0);//底部显示
        }
        popupWindow.setOnDismissListener(onDismissListener);//弹窗消失监听
    }

    @Override
    public void onClick(View view) {
        switch (view.getId()){
            case R.id.tv_take_photo:
                //拍照
                if (onClickListener != null){
                    onClickListener.onClick(TAKE_PHOTO);
                }
                break;
            case R.id.tv_select_photo:
                //选择相册
                if (onClickListener != null){
                    onClickListener.onClick(SELECT_PHOTO);
                }
                break;
        }
        //点击任何一项就关闭弹窗
        if (isDismissAfterClick && popupWindow.isShowing()){
            popupWindow.dismiss();
        }
    }
}
