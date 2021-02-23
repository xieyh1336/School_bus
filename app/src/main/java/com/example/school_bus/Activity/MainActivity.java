package com.example.school_bus.Activity;

import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.KeyEvent;
import android.view.View;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.view.GravityCompat;
import androidx.drawerlayout.widget.DrawerLayout;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.load.resource.bitmap.CircleCrop;
import com.bumptech.glide.request.RequestOptions;
import com.example.school_bus.Fragment.FragmentOnKeyListener;
import com.example.school_bus.Fragment.Main.MapFragment;
import com.example.school_bus.Fragment.Main.MapSideFragment;
import com.example.school_bus.Fragment.MainFragment;
import com.example.school_bus.Fragment.OfflineMapFragment;
import com.example.school_bus.MyApp;
import com.example.school_bus.R;
import com.example.school_bus.Utils.ImageUtil;
import com.example.school_bus.Utils.MyLog;

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
 * 侧边栏：{@link MapSideFragment}
 */
public class MainActivity extends BaseActivity {

    private static String TAG = "MainActivity";
    @BindView(R.id.vp)
    ViewPager2 vp;
    @BindView(R.id.iv_header)
    ImageView ivHeader;
    @BindView(R.id.dl_map)
    DrawerLayout dlMap;
    @BindView(R.id.fl_side)
    FrameLayout flSide;
    @BindView(R.id.ll_top)
    LinearLayout llTop;
    @BindView(R.id.iv_header2)
    ImageView ivHeader2;
    @BindView(R.id.tv_header)
    TextView tvHeader;
    private boolean isFirst = true;

    //viewPager
    public ArrayList<Fragment> fragmentList = new ArrayList<>();
    public MapSideFragment mapSideFragment = MapSideFragment.newInstance();//侧边栏

    public MainFragment mainFragment = MainFragment.newInstance();//首页fragment
    public OfflineMapFragment offlineMapFragment = OfflineMapFragment.newInstance();//离线地图

    private long secondBackTime;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        ButterKnife.bind(this);
        init();
        setListener();
    }

    public void init() {
        updateHead(false);

        fragmentList.add(mainFragment);
        fragmentList.add(offlineMapFragment);

        ivHeader.setVisibility(View.VISIBLE);
        ivHeader2.setVisibility(View.GONE);
        tvHeader.setVisibility(View.GONE);

        //第二个参数懒加载
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(fragmentList.size());
        vp.setUserInputEnabled(false);//禁止滑动

        //添加侧边栏
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(flSide.getId(), mapSideFragment);
        fragmentTransaction.commit();
    }

    public void setListener() {
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

    /**
     * 选择页面
     *
     * @param i 页码
     */
    public void selectPager(int i) {
        vp.setCurrentItem(i, false);
        if (i == 0){
            ivHeader.setVisibility(View.VISIBLE);
            ivHeader2.setVisibility(View.GONE);
            tvHeader.setVisibility(View.GONE);
        } else if (i == 1){
            ivHeader.setVisibility(View.GONE);
            ivHeader2.setVisibility(View.VISIBLE);
            tvHeader.setVisibility(View.VISIBLE);
            tvHeader.setText("离线地图");
        }
    }

    /**
     * 获取当前页面
     *
     * @return 页码
     */
    public int getCurrentPager() {
        return vp.getCurrentItem();
    }

    /**
     * 关闭侧滑菜单
     */
    public void closeSideView() {
        dlMap.closeDrawer(GravityCompat.START);
    }

    /**
     * 加载头像
     *
     * @param isUp 是否更新了头像
     */
    public void updateHead(boolean isUp) {
        MyLog.e(TAG, "MapActivity加载头像");
        if (isUp) {
            Glide.with(this)
                    .load(ImageUtil.getHeadUrl(MyApp.getHead()))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .skipMemoryCache(true)
                    .diskCacheStrategy(DiskCacheStrategy.NONE)
                    .error(R.drawable.ic_header)
                    .into(ivHeader);
            mapSideFragment.updateHead();
        } else {
            Glide.with(this)
                    .load(ImageUtil.getHeadUrl(MyApp.getHead()))
                    .apply(RequestOptions.bitmapTransform(new CircleCrop()))
                    .error(R.drawable.ic_header)
                    .into(ivHeader);
        }

    }

    @Override
    protected void onResume() {
        super.onResume();
        new Handler().postDelayed(() -> {
            if (isFirst) {
                showToast("欢迎您 " + MyApp.getUserName());
                isFirst = false;
            }
        }, 2000);
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听MoreFragment的返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            if (mainFragment != null && ((FragmentOnKeyListener) mainFragment).onKeyDown(keyCode, event)) {
                //fragment监听了，则不往下传递事件
                return true;
            }
        }
        return super.onKeyDown(keyCode, event);
    }

    @Override
    public void onBackPressed() {
        long firstBackTime = System.currentTimeMillis();
        if (dlMap.isDrawerOpen(GravityCompat.START)) {
            //如果侧边栏打开的时候，先收起侧边栏
            closeSideView();
        } else {
            if (getCurrentPager() == 0) {
                if (firstBackTime - secondBackTime > 2000) {
                    showToast("再按一次返回桌面");
                    secondBackTime = firstBackTime;
                } else {
                    //返回桌面
                    Intent home = new Intent(Intent.ACTION_MAIN);
                    home.addCategory(Intent.CATEGORY_HOME);
                    startActivity(home);
                }
            } else {
                selectPager(0);
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
    }

    @OnClick({R.id.ll_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.ll_top:
                dlMap.openDrawer(GravityCompat.START);
                break;
        }
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MapFragment.MAP_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    MyLog.e(TAG, "所有权限已同意");
                    mainFragment.mapFragment.init();
                } else {
                    showToast("必须同意所有权限才能使用本程序!");
                }
            } else {
                showToast("发生未知错误");
            }
        }
    }

    private class MyPagerAdapter extends FragmentStateAdapter {

        public MyPagerAdapter(@NonNull FragmentActivity fragmentActivity) {
            super(fragmentActivity);
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
