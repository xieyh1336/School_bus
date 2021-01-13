package com.example.school_bus.Activity;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import com.example.school_bus.Entity.TabEntityData;
import com.example.school_bus.Fragment.FragmentOnKeyListener;
import com.example.school_bus.Fragment.MapFragment;
import com.example.school_bus.Fragment.MapSideFragment;
import com.example.school_bus.Fragment.MoreFragment;
import com.example.school_bus.Fragment.NavigationFragment;
import com.example.school_bus.R;
import com.example.school_bus.View.MyViewPager;
import com.flyco.tablayout.CommonTabLayout;
import com.flyco.tablayout.listener.CustomTabEntity;
import com.flyco.tablayout.listener.OnTabSelectListener;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-06 16:37
 * @类名 MapActivity
 * @所在包 com\example\school_bus\Activity\MapActivity.java
 * 主页面
 */
public class MapActivity extends BaseActivity {

    @BindView(R.id.vp)
    MyViewPager vp;
    @BindView(R.id.tl)
    CommonTabLayout tl;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.dl_map)
    DrawerLayout dlMap;
    @BindView(R.id.fl_side)
    FrameLayout flSide;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String[] titles = {"地图", "导航", "更多"};
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private MoreFragment moreFragment = new MoreFragment();
    private int[] mIconUnselectIds =
            {R.mipmap.tab_map_unselect, R.mipmap.tab_navigation_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds =
            {R.mipmap.tab_map_select, R.mipmap.tab_navigation_select, R.mipmap.tab_more_select};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        init();
        setListener();
    }

    public void init() {
        fragmentList.add(MapFragment.getInstance());
        fragmentList.add(NavigationFragment.getInstance());
        fragmentList.add(moreFragment);

        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntityData(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(3);
        vp.setStopForViewPager(true);
        tl.setTabData(mTabEntities);

        //添加侧边栏
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(flSide.getId(), MapSideFragment.getInstance());
        fragmentTransaction.commit();
    }

    public void setListener() {

        //相互绑定
        tl.setOnTabSelectListener(new OnTabSelectListener() {
            @Override
            public void onTabSelect(int position) {
                vp.setCurrentItem(position);
            }

            @Override
            public void onTabReselect(int position) {

            }
        });
        vp.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                tl.setCurrentTab(position);
            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听MoreFragment的返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (moreFragment != null && ((FragmentOnKeyListener) moreFragment).onKeyDown(keyCode, event)) {
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @OnClick({R.id.iv_header})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_header:
                dlMap.openDrawer(GravityCompat.START);
                break;
        }
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public int getCount() {
            return fragmentList.size();
        }

        @Override
        public CharSequence getPageTitle(int position) {
            return titles[position];
        }

        @NonNull
        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
