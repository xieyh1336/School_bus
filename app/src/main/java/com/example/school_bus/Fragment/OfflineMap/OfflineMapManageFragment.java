package com.example.school_bus.Fragment.OfflineMap;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.R;
import com.example.school_bus.View.DownloadView;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-05 14:21
 * @类名 OfflineMapManageFragment
 * @所在包 com\example\school_bus\Fragment\OfflineMap\OfflineMapManageFragment.java
 * 离线地图下载管理
 */
public class OfflineMapManageFragment extends BaseVp2LazyLoadFragment {

    @BindView(R.id.test)
    DownloadView test;

    public static OfflineMapManageFragment newInstance() {
        return new OfflineMapManageFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_map_manage, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        test.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (test.getState() == DownloadView.START){
                    test.setState(DownloadView.PAUSE);
                } else {
                    test.setState(DownloadView.START);
                }
            }
        });
    }
}