package com.example.school_bus.Fragment;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.baidu.mapapi.map.offline.MKOfflineMapListener;
import com.example.school_bus.Activity.MainActivity;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Fragment.Main.MapSideFragment;
import com.example.school_bus.Fragment.OfflineMap.OfflineMapListFragment;
import com.example.school_bus.Fragment.OfflineMap.OfflineMapManageFragment;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;
import com.google.android.material.tabs.TabLayout;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-22 15:19
 * @类名 OfflineMapFragment
 * @所在包 com\example\school_bus\Fragment\OfflineMapFragment.java
 * 离线地图
 */
public class OfflineMapFragment extends BaseVp2LazyLoadFragment {

    private static String TAG = "OfflineMapFragment";
    @BindView(R.id.tab)
    TabLayout tab;
    @BindView(R.id.vp)
    ViewPager2 vp;
    //tab
    private List<Fragment> fragmentList = new ArrayList<>();
    private OfflineMapManageFragment offlineMapManageFragment = OfflineMapManageFragment.newInstance();//下载管理
    private OfflineMapListFragment offlineMapListFragment = OfflineMapListFragment.newInstance();//下载列表

    private MKOfflineMap mkOfflineMap;//离线地图
    private OfflineListener offlineListener;//下载监听接口

    public static OfflineMapFragment newInstance() {
        return new OfflineMapFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        init();
        setListener();
    }

    private void init(){
        //离线地图测试
        if (mkOfflineMap == null){
            mkOfflineMap = new MKOfflineMap();
        }
        mkOfflineMap.init(new MKOfflineMapListener() {
            @Override
            public void onGetOfflineMapState(int type, int state) {
                //更新过程中的回调进度，可查看更新进度、新离线地图安装、版本更新提示。
                //type - 事件类型:
                // MKOfflineMap.TYPE_NEW_OFFLINE,
                // MKOfflineMap.TYPE_DOWNLOAD_UPDATE,
                // MKOfflineMap.TYPE_VER_UPDATE.
                //state - 事件状态: 当type为TYPE_NEW_OFFLINE时，表示新安装的离线地图数目. 当type为TYPE_DOWNLOAD_UPDATE时，表示更新的城市ID.
                if (offlineListener != null){
                    //回调到离线地图
                    offlineListener.mapListener(type, state);
                }
            }
        });
        //获取可下载的城市
        List<MKOLSearchRecord> list = mkOfflineMap.getOfflineCityList();
        MyLog.e(TAG, "支持离线地图的城市列表：-----------------------------------");
        for (int i = 0; i < list.size(); i++) {
            MyLog.e(TAG, "城市名：" + list.get(i).cityName);
            MyLog.e(TAG, "子城市名：" + list.get(i).childCities);
            MyLog.e(TAG, "城市ID：" + list.get(i).cityID);
            MyLog.e(TAG, "城市类型：" + list.get(i).cityType);//城市类型0:全国；1：省份；2：城市,如果是省份，可以通过childCities得到子城市列表
            MyLog.e(TAG, "数据大小：" + list.get(i).dataSize);
            MyLog.e(TAG, "-----------------------------------");
        }
        List<MKOLUpdateElement> list1 = mkOfflineMap.getAllUpdateInfo();
        MyLog.e(TAG, "离线地图信息：-----------------------------------");
        //未定义：UNDEFINED = 0;
        //正在下载：DOWNLOADING = 1;
        //等待下载：WAITING = 2;
        //已暂停：SUSPENDED = 3;
        //完成：FINISHED = 4;
        //校验失败：eOLDSMd5Error = 5;
        //网络异常：eOLDSNetError = 6;
        //读写异常：eOLDSIOError = 7;
        //Wifi网络异常：eOLDSWifiError = 8;
        //数据错误，需重新下载：eOLDSFormatError = 9;
        //离线包正在导入：eOLDSInstalling = 10;
        if (list1 != null){
            for (int i = 0; i < list1.size(); i++){
                MyLog.e(TAG, "城市名：" + list1.get(i).cityName);
                MyLog.e(TAG, "城市ID：" + list1.get(i).cityID);
                MyLog.e(TAG, "城市中心点坐标经度：" + list1.get(i).geoPt.longitude);
                MyLog.e(TAG, "城市中心点坐标纬度：" + list1.get(i).geoPt.latitude);
                MyLog.e(TAG, "离线包地图层级：" + list1.get(i).level);
                MyLog.e(TAG, "下载比率：" + list1.get(i).ratio);
                MyLog.e(TAG, "已下载数据大小：" + list1.get(i).size);
                MyLog.e(TAG, "下载状态：" + list1.get(i).status);
                MyLog.e(TAG, "是否为更新：" + list1.get(i).update);
            }
        }

        //tab
        TabLayout.Tab tab1 = tab.newTab();
        tab1.setText("下载管理");
        tab.addTab(tab1);

        tab1 = tab.newTab();
        tab1.setText("城市列表");
        tab.addTab(tab1);

        fragmentList.add(offlineMapManageFragment);
        fragmentList.add(offlineMapListFragment);
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);
        vp.setAdapter(myPagerAdapter);
    }

    private void setListener(){
        tab.addOnTabSelectedListener(new TabLayout.OnTabSelectedListener() {
            @Override
            public void onTabSelected(TabLayout.Tab tab) {
                vp.setCurrentItem(tab.getPosition());
            }

            @Override
            public void onTabUnselected(TabLayout.Tab tab) {

            }

            @Override
            public void onTabReselected(TabLayout.Tab tab) {

            }
        });
        vp.registerOnPageChangeCallback(new ViewPager2.OnPageChangeCallback() {
            @Override
            public void onPageSelected(int position) {
                super.onPageSelected(position);
                Objects.requireNonNull(tab.getTabAt(position)).select();
            }
        });
    }

    public MKOfflineMap getMkOfflineMap(){
        return mkOfflineMap;
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null){
            //刷新列表样式
            ((MainActivity) getActivity()).mapSideFragment.initList();
        }
    }

    public void setOfflineListener(OfflineListener offlineListener) {
        this.offlineListener = offlineListener;
    }

    public interface OfflineListener{
        void mapListener(int type, int state);
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(@NonNull Fragment fragment) {
            super(fragment);
        }

        @NonNull
        @Override
        public Fragment createFragment(int position) {
            return fragmentList.get(position);
        }

        @Override
        public int getItemCount() {
            return fragmentList.size();
        }
    }
}