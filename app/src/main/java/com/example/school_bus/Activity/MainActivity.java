package com.example.school_bus.Activity;

import android.content.Intent;
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

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.school_bus.Entity.TabEntityData;
import com.example.school_bus.Fragment.FragmentOnKeyListener;
import com.example.school_bus.Fragment.MapFragment;
import com.example.school_bus.Fragment.MapSideFragment;
import com.example.school_bus.Fragment.MoreFragment;
import com.example.school_bus.Fragment.OrderFragment;
import com.example.school_bus.MyApp;
import com.example.school_bus.R;
import com.example.school_bus.Utils.ImageUtil;
import com.example.school_bus.Utils.MyLog;
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
 * @类名 MainActivity
 * @所在包 com\example\school_bus\Activity\MainActivity.java
 * 主页面
 * fragment：
 * {@link MapFragment}第一页，地图
 * {@link OrderFragment}第二页，导航
 * {@link MoreFragment}第三页，更多
 * 侧边栏：{@link MapSideFragment}
 */
public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";
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
    private String[] titles = {"地图", "预约", "更多"};
    private ArrayList<CustomTabEntity> mTabEntities = new ArrayList<>();
    private MoreFragment moreFragment = MoreFragment.getInstance();
    private long secondBackTime;
    private int[] mIconUnSelectIds =
            {R.mipmap.tab_map_unselect, R.mipmap.tab_navigation_unselect, R.mipmap.tab_more_unselect};
    private int[] mIconSelectIds =
            {R.mipmap.tab_map_select, R.mipmap.tab_navigation_select, R.mipmap.tab_more_select};

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setListener();
        showToast("欢迎您 " + MyApp.getUserName());
    }

    public void init() {
        updateHead();

        fragmentList.add(MapFragment.getInstance());
        fragmentList.add(OrderFragment.getInstance());
        fragmentList.add(moreFragment);

        for (int i = 0; i < titles.length; i++) {
            mTabEntities.add(new TabEntityData(titles[i], mIconSelectIds[i], mIconUnSelectIds[i]));
        }

        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(getSupportFragmentManager());
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
        //侧滑菜单监听
        dlMap.addDrawerListener(new DrawerLayout.DrawerListener() {
            @Override
            public void onDrawerSlide(@NonNull View drawerView, float slideOffset) {

            }

            @Override
            public void onDrawerOpened(@NonNull View drawerView) {
                //打开了侧滑菜单
                MyLog.e(TAG, "打开了侧滑菜单");
            }

            @Override
            public void onDrawerClosed(@NonNull View drawerView) {
                //关闭了侧滑菜单
                MyLog.e(TAG, "关闭了侧滑菜单");
            }

            @Override
            public void onDrawerStateChanged(int newState) {

            }
        });
    }

    public void updateHead(){
        MyLog.e(TAG, "MapActivity加载头像");
        Glide.with(this)
                .load(ImageUtil.getHeadUrl(MyApp.getHead()))
                .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                .skipMemoryCache(true)
                .diskCacheStrategy(DiskCacheStrategy.NONE)
                .error(R.drawable.ic_header)
                .into(ivHeader);
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

    @Override
    public void onBackPressed() {
        long firstBackTime = System.currentTimeMillis();
        if (firstBackTime - secondBackTime > 2000){
            showToast("再按一次返回桌面");
            secondBackTime = firstBackTime;
        }else {
            //返回桌面
            Intent home = new Intent(Intent.ACTION_MAIN);
            home.addCategory(Intent.CATEGORY_HOME);
            startActivity(home);
        }
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
