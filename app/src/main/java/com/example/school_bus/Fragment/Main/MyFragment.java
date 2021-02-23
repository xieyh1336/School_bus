package com.example.school_bus.Fragment.Main;

import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.school_bus.R;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-04 12:00
 * @类名 MyFragment
 * @所在包 com\example\school_bus\Fragment\Main\MyFragment.java
 * 我的页
 */
public class MyFragment extends Fragment {

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_my, container, false);
        return view;
    }
}