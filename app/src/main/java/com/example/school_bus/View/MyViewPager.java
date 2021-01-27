package com.example.school_bus.View;

import android.content.Context;
import android.util.AttributeSet;
import android.view.MotionEvent;

import androidx.viewpager.widget.ViewPager;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-23 11:52
 * @类名 MyViewPager
 * @所在包 com\example\school_bus\View\MyViewPager.java
 * 不可滑动的viewPager
 */
public class MyViewPager extends ViewPager {

    private boolean mIsStop = false;//限制不能左右滑动
    private boolean mStartAnimation = true;//是否启用滑动动画

    public void setStopForViewPager(boolean isStop) {
        mIsStop = isStop;
    }

    public void setStartAnimation(boolean isStop) {
        mStartAnimation = isStop;
    }

    public MyViewPager(Context context) {
        super(context);
    }

    public MyViewPager(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    @Override
    public boolean onTouchEvent(MotionEvent ev) {
        if (mIsStop) {
            return false;
        } else {
            return super.onTouchEvent(ev);
        }
    }

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {
        if (mIsStop) {
            return false;
        } else {
            return super.onInterceptTouchEvent(ev);
        }
    }

    //去除页面切换时的滑动翻页效果
    @Override
    public void setCurrentItem(int item, boolean smoothScroll) {
        super.setCurrentItem(item, smoothScroll);
    }

    @Override
    public void setCurrentItem(int item) {
        super.setCurrentItem(item, mStartAnimation);
    }
}