package com.example.school_bus.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.school_bus.Activity.StudentActivity;
import com.example.school_bus.Entity.TabEntityData;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Fragment.Main.MapFragment;
import com.example.school_bus.Fragment.Main.MoreFragment;
import com.example.school_bus.Fragment.Main.OrderFragment;
import com.example.school_bus.R;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-22 15:00
 * @类名 MainFragment
 * @所在包 com\example\school_bus\Fragment\Main\MainFragment.java
 * 首页fragment
 * {@link MapFragment}第一页，地图
 * {@link OrderFragment}第二页，导航
 * {@link MoreFragment}第三页，更多
 */
public class MainFragment extends BaseVp2LazyLoadFragment implements FragmentOnKeyListener {


    @BindView(R.id.vp)
    ViewPager2 vp;
    @BindView(R.id.tl)
    CommonTabLayout tl;
    //标题栏
    private List<String> titles = new ArrayList<>();
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private List<Integer> mIconSelectIds = new ArrayList<>();
    private List<Integer> mIconUnSelectIds = new ArrayList<>();
    //viewPager
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    public MapFragment mapFragment = MapFragment.newInstance();
    private OrderFragment orderFragment = OrderFragment.newInstance();
    private MoreFragment moreFragment = MoreFragment.newInstance();

    public static MainFragment newInstance() {
        return new MainFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_main, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    /**
     * 懒加载，只加载一次
     */
    @Override
    public void lazyLoad() {
        init();
        setListener();
    }

    private void init() {
        titles.add("地图");
        titles.add("预约");
        titles.add("更多");

        mIconSelectIds.add(R.mipmap.tab_map_select);
        mIconSelectIds.add(R.mipmap.tab_navigation_select);
        mIconSelectIds.add(R.mipmap.tab_more_select);

        mIconUnSelectIds.add(R.mipmap.tab_map_unselect);
        mIconUnSelectIds.add(R.mipmap.tab_navigation_unselect);
        mIconUnSelectIds.add(R.mipmap.tab_more_unselect);

        for (int i = 0; i < titles.size(); i++) {
            mTabEntities.add(new TabEntityData(titles.get(i), mIconSelectIds.get(i), mIconUnSelectIds.get(i)));
        }

        fragmentList.add(mapFragment);
        fragmentList.add(orderFragment);
        fragmentList.add(moreFragment);

        //第二个参数懒加载
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(fragmentList.size());
        vp.setUserInputEnabled(false);//禁止滑动
        tl.setTabData(mTabEntities);
    }

    private void setListener(){
        //tabLayout监听
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (getActivity() != null){
            //刷新列表样式
            ((StudentActivity) getActivity()).mapSideFragment.initList();
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return moreFragment != null && ((FragmentOnKeyListener) moreFragment).onKeyDown(keyCode, event);
        }
        return false;
    }

    /**
     * 分页适配器
     */
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