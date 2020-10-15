package com.example.school_bus.Fragment;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.school_bus.Activity.WebActivity;
import com.example.school_bus.Adapter.NewsRecyclerviewAdapter;
import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Mvp.MoreFMvp;
import com.example.school_bus.Presenter.MoreFPresenter;
import com.example.school_bus.R;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZHolderCreator;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 更多界面
 */
public class MoreFragment extends BaseFragment implements MoreFMvp.view, OnRefreshListener, OnLoadMoreListener {
    private static MoreFragment moreFragment;
    @BindView(R.id.bannerView)
    MZBannerView bannerView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.recyclerView)
    RecyclerView recyclerView;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    private MoreFPresenter moreFPresenter;
    private NewsRecyclerviewAdapter newsRecyclerviewAdapter;
    private int page = 2;
    private boolean isLoadMore = false;

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
        refresh.setOnLoadMoreListener(this);
        refresh.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        recyclerView.setLayoutManager(new LinearLayoutManager(getContext(), RecyclerView.VERTICAL, false));
    }

    public void initData() {
        //获取轮播图新闻
        moreFPresenter.getNews("1", "7", false);
        //获取下方新闻
        Handler handler = new Handler();
        Runnable runnable = new Runnable() {
            @Override
            public void run() {
                moreFPresenter.getNews(String.valueOf(page), "7", isLoadMore);
            }
        };
        handler.postDelayed(runnable, 200);
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
        //设置轮播图指示器
        bannerView.setIndicatorAlign(MZBannerView.IndicatorAlign.RIGHT);
        //设置轮播图内容
        bannerView.setPages(data.getResult(), new MZHolderCreator() {
            @Override
            public MZViewHolder createViewHolder() {
                return new ViewPagerHolder();
            }
        });
    }

    @Override
    public void getNewsResult2(NewsData data, boolean isLoadMore) {
        if (newsRecyclerviewAdapter != null) {
            if (isLoadMore) {
                newsRecyclerviewAdapter.addData(data.getResult());
            } else {
                newsRecyclerviewAdapter.setNewData(data.getResult());
            }
            return;
        }
        newsRecyclerviewAdapter = new NewsRecyclerviewAdapter(R.layout.item_more_news_recycle_view, data.getResult(), getContext());
        recyclerView.setAdapter(newsRecyclerviewAdapter);
        newsRecyclerviewAdapter.setOnItemClickListener(new NewsRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("Url", data.getResult().get(position).getPath());
                startActivity(intent);
            }
        });
    }

    @Override
    public void onError(Throwable e, int i) {
        switch (i) {
            case 0:
                refresh.setVisibility(View.GONE);
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                break;
        }
    }

    @Override
    public void onComplete(int i) {
        switch (i) {
            case 0:
                refresh.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                if (isLoadMore) {
                    refresh.finishLoadMore();
                } else {
                    refresh.finishRefresh();
                }
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 2;
        isLoadMore = false;
        initData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page += 1;
        //获取下方新闻
        isLoadMore = true;
        moreFPresenter.getNews(String.valueOf(page), "7", isLoadMore);
    }

    @OnClick({R.id.error_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                refresh.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                break;
        }
    }

    public static final class ViewPagerHolder implements MZViewHolder<NewsData.ResultBean> {
        private ImageView imageView;
        private TextView textView;

        @Override
        public View createView(Context context) {
            View view = LayoutInflater.from(context).inflate(R.layout.normal_banner_item, null);
            imageView = view.findViewById(R.id.normal_banner_image);
            textView = view.findViewById(R.id.textView);
            return view;
        }

        @Override
        public void onBind(Context context, int position, NewsData.ResultBean data) {
            Glide.with(context).load(data.getImage()).into(imageView);
            textView.setText(data.getTitle());
            textView.getPaint().setFakeBoldText(true);
        }
    }
}
