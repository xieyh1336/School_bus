package com.example.school_bus.Fragment;

import android.os.Bundle;
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

public class MoreFragment extends BaseFragment {

    private static MoreFragment moreFragment;
    //boomMenu
    private static int imageResourceIndex = 0;

    @BindView(R.id.vp)
    MyViewPager vp;
    @BindView(R.id.bmb)
    BoomMenuButton bmb;
    @BindView(R.id.tv_title)
    TextView tvTitle;
    private MyPagerAdapter myPagerAdapter;
    private ArrayList<Fragment> fragmentList = new ArrayList<>();
    private String[] titles = {"新闻", "美图"};
    private NewsFragment newsFragment = new NewsFragment();
    private PictureFragment pictureFragment = new PictureFragment();

    public static MoreFragment getInstance() {
        if (moreFragment == null) {
            moreFragment = new MoreFragment();
        }
        return moreFragment;
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
        fragmentList.add(pictureFragment);
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
                            tvTitle.setText(titles[index]);
                        }
                    });
            bmb.addBuilder(builder);
        }
        //viewPager
        myPagerAdapter = new MyPagerAdapter(getChildFragmentManager());
        vp.setAdapter(myPagerAdapter);
        vp.setOffscreenPageLimit(3);
        //进入默认显示第0个fragment
        vp.setCurrentItem(0);
        tvTitle.setText(titles[0]);

    }

    static int getImageResource() {
        if (imageResourceIndex >= imageResources.length) imageResourceIndex = 0;
        return imageResources[imageResourceIndex++];
    }

    private static int[] imageResources = new int[]{
            R.drawable.bat,
            R.drawable.bear,
    };

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
