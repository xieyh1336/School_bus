package com.example.school_bus.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.viewpager2.adapter.FragmentStateAdapter;
import androidx.viewpager2.widget.ViewPager2;

import com.example.school_bus.Fragment.LazyLoad.ViewPager2LazyLoadFragment;
import com.example.school_bus.Fragment.More.NewsFragment;
import com.example.school_bus.Fragment.More.PicturesFragment;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;
import com.nightonke.boommenu.BoomButtons.OnBMClickListener;
import com.nightonke.boommenu.BoomButtons.TextInsideCircleButton;
import com.nightonke.boommenu.BoomMenuButton;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 14:07
 * @类名 MoreFragment
 * @所在包 com\example\school_bus\Fragment\MoreFragment.java
 * 更多页面主页
 */
public class MoreFragment extends ViewPager2LazyLoadFragment implements FragmentOnKeyListener {

    private static String TAG = "MoreFragment";
    @BindView(R.id.vp)
    ViewPager2 vp;
    @BindView(R.id.bmb)
    BoomMenuButton bmb;
    //boomMenu
    private String[] titles = {"新闻", "美图"};
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private int irIndex = 0;
    private int[] imageResources = new int[]{R.drawable.bat, R.drawable.bear};
    private NewsFragment newsFragment = NewsFragment.getInstance();
    private PicturesFragment picturesFragment = PicturesFragment.getInstance();

    public static MoreFragment newInstance() {
        return new MoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        ButterKnife.bind(this, view);
        initFragment();
        initView();
        return view;
    }

    @Override
    public void lazyLoad() {
        MyLog.e(TAG, "MoreFragment懒加载");
    }

    public void initFragment() {
        fragmentList.add(newsFragment);
        fragmentList.add(picturesFragment);
    }

    public void initView() {
        //构建BoomMenu
        for (int i = 0; i < bmb.getPiecePlaceEnum().pieceNumber(); i++) {
            TextInsideCircleButton.Builder builder = new TextInsideCircleButton.Builder()
                    .normalImageRes(getImageResource())
                    .normalText(titles[i])
                    .listener(new OnBMClickListener() {
                        @Override
                        public void onBoomButtonClick(int index) {
                            //第二个参数是禁止滑动动画
                            vp.setCurrentItem(index, false);
                        }
                    });
            bmb.addBuilder(builder);
        }
        //viewPager
        MyPagerAdapter myPagerAdapter = new MyPagerAdapter(this);
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(3);
        vp.setUserInputEnabled(false);//禁止滑动
        //进入默认显示第0个fragment
        vp.setCurrentItem(0);
    }

    private int getImageResource() {
        if (irIndex >= imageResources.length) irIndex = 0;
        return imageResources[irIndex++];
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听PicturesFragment的返回键
        if (keyCode == KeyEvent.KEYCODE_BACK) {
            return picturesFragment != null && ((FragmentOnKeyListener) picturesFragment).onKeyDown(keyCode, event);
        }
        return false;
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