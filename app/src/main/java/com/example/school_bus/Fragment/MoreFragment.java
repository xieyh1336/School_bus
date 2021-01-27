package com.example.school_bus.Fragment;

import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentPagerAdapter;

import com.example.school_bus.R;
import com.example.school_bus.View.MyViewPager;
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
public class MoreFragment extends BaseFragment implements FragmentOnKeyListener{

    //boomMenu
    private static int imageResourceIndex = 0;

    @BindView(R.id.vp)
    MyViewPager vp;
    @BindView(R.id.bmb)
    BoomMenuButton bmb;
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String[] titles = {"新闻", "美图"};
    private NewsFragment newsFragment = NewsFragment.getInstance();
    private PicturesFragment picturesFragment = PicturesFragment.getInstance();

    public static MoreFragment getInstance(){
        return new MoreFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_more, container, false);
        initFragment();
        initView(view);
        return view;
    }

    public void initFragment() {
        fragmentList.add(newsFragment);
        fragmentList.add(picturesFragment);
    }

    public void initView(View view) {
        ButterKnife.bind(this, view);
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
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(3);
        vp.setStopForViewPager(true);
        //进入默认显示第0个fragment
        vp.setCurrentItem(0);

    }

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
    };

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //监听PicturesFragment的返回键
        if (keyCode == KeyEvent.KEYCODE_BACK){
            return picturesFragment != null && ((FragmentOnKeyListener) picturesFragment).onKeyDown(keyCode, event);
        }
        return false;
    }

    private class MyPagerAdapter extends FragmentPagerAdapter {
        public MyPagerAdapter(FragmentManager fm) {
            //noinspection deprecation
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
