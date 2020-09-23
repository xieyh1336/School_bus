package com.example.school_bus.Activity;

import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;
import androidx.viewpager.widget.ViewPager;

import com.example.school_bus.Entity.TabEntity;
import com.example.school_bus.Fragment.MapFragment;
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

public class MapActivity extends BaseActivity {

    @BindView(R.id.vp)
    MyViewPager vp;
    @BindView(R.id.tl)
    CommonTabLayout tl;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String[] titles = {"地图", "导航", "更多"};
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private int[] mIconUnselectIds =
            {R.mipmap.tab_map_unselect, R.mipmap.tab_navigation_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds =
            {R.mipmap.tab_map_select, R.mipmap.tab_navigation_select, R.mipmap.tab_more_select};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_map);
        ButterKnife.bind(this);
        initFragment();
        initView();
    }

    public void initFragment(){
        fragmentList.add(MapFragment.getInstance());
        fragmentList.add(NavigationFragment.getInstance());
        fragmentList.add(MoreFragment.getInstance());
    }

    public void initView() {
        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntity(titles[i], mIconSelectIds[i], mIconUnselectIds[i]));
        }

        myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(3);
        vp.setStopForViewPager(true);
        tl.setTabData(mTabEntities);
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

        @Override
        public Fragment getItem(int position) {
            return fragmentList.get(position);
        }
    }
}
