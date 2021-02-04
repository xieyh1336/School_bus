package com.example.school_bus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.school_bus.Fragment.LazyLoad.ViewPager2LazyLoadFragment;
import com.example.school_bus.Utils.MyLog;

/**
 * 预约界面
 */
public class OrderFragment extends ViewPager2LazyLoadFragment {

    private static String TAG = "OrderFragment";

    public static OrderFragment newInstance(){
        return new OrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void lazyLoad() {
        MyLog.e(TAG, "OrderFragment懒加载");
    }
}
