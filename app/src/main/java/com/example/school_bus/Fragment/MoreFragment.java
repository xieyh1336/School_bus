package com.example.school_bus.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.example.school_bus.Activity.WebActivity;
import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Mvp.MoreFMvp;
import com.example.school_bus.Presenter.MoreFPresenter;
import com.example.school_bus.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * 更多界面
 */
public class MoreFragment extends BaseFragment implements MoreFMvp.view, OnRefreshListener {
    @BindView(R.id.bannerView)
    MZBannerView bannerView;
    private static MoreFragment moreFragment;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private MoreFPresenter moreFPresenter;

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
        ButterKnife.bind(this, view);
        moreFPresenter = new MoreFPresenter(this);
        initView(view);
        initData();
        return view;
    }

    public void initView(View view) {
        refresh.setOnRefreshListener(this);
    }

    public void initData() {
        //获取轮播图新闻
        moreFPresenter.getNews("1", "7");
    }

    @Override
    public void onPause() {
        super.onPause();
        bannerView.pause();
    }

    @Override
    public void onResume() {
        super.onResume();
        bannerView.start();
    }

    @Override
    public void getNewsResult(NewsData data) {
        //设置轮播图的点击事件
        bannerView.setBannerPageClickListener(new MZBannerView.BannerPageClickListener() {
            @Override
            public void onPageClick(View view, int position) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("Url", data.getResult().get(position).getPath());
                startActivity(intent);
            }
        });
        bannerView.setPages(data.getResult(), new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new ViewPagerHolder();
            }
        });
    }

    @Override
    public void onError(Throwable e, int i) {
        switch (i) {
            case 0:
                break;
        }
    }

    @Override
    public void onComplete(int i) {
        switch (i) {
            case 0:
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        initData();
        refreshLayout.finishRefresh();
    }

    public static final class ViewPagerHolder implements MZViewHolder<NewsData.ResultBean> {
        private ImageView mImageView;
        private TextView mDesc;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.normal_banner_item, null);
            mImageView = (ImageView) view.findViewById(R.id.normal_banner_image);
            mDesc = (TextView) view.findViewById(R.id.page_desc);
            return view;
        }

        @Override
        public void onBind(Context context, int position, NewsData.ResultBean data) {
            Glide.with(context).load(data.getImage()).into(mImageView);
            mDesc.setText(data.getTitle());
        }
    }

}
