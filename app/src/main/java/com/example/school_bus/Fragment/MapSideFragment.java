package com.example.school_bus.Fragment;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.school_bus.R;
import com.example.school_bus.View.SwitchView;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-13 14:19
 * @类名 MapSideFragment
 * @所在包 com\example\school_bus\Fragment\MapSideFragment.java
 * 主页面侧边栏
 */
public class MapSideFragment extends BaseFragment {
    private static MapSideFragment mapSideFragment;

    @BindView(R.id.switch1)
    SwitchView switch1;

    public static MapSideFragment getInstance(){
        if (mapSideFragment == null){
            mapSideFragment = new MapSideFragment();
        }
        return mapSideFragment;
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map_side, container, false);
        ButterKnife.bind(this, view);
        init();
        return view;
    }

    private void init(){
        switch1.toggleSwitch(false);
        switch1.setColor(0xFF1878FF,0xFFFFFFFF);
        switch1.setOnStateChangedListener(new SwitchView.OnStateChangedListener() {
            @Override
            public void toggleToOn(SwitchView view) {
                view.toggleSwitch(true);
                Intent intent = new Intent("Map");
                intent.putExtra("type", 0);
                intent.putExtra("state", true);
                Objects.requireNonNull(getActivity()).sendBroadcast(intent);
            }

            @Override
            public void toggleToOff(SwitchView view) {
                view.toggleSwitch(false);
                Intent intent = new Intent("Map");
                intent.putExtra("type", 0);
                intent.putExtra("state", false);
                Objects.requireNonNull(getActivity()).sendBroadcast(intent);
            }
        });
    }
}
