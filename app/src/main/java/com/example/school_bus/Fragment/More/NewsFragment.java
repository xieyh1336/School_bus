package com.example.school_bus.Fragment.More;

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
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.school_bus.Activity.WebActivity;
import com.example.school_bus.Adapter.NewsRecyclerviewAdapter;
import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.Fragment.LazyLoad.ViewPager2LazyLoadFragment;
import com.example.school_bus.Mvp.NewsFMvp;
import com.example.school_bus.Presenter.NewsFPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;
import com.zhouwei.mzbanner.MZBannerView;
import com.zhouwei.mzbanner.holder.MZViewHolder;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2020-11-19 17:32
 * @类名 NewsFragment
 * @所在包 com\example\school_bus\Fragment\NewsFragment.java
 * 更多页面，新闻分页
 */
public class NewsFragment extends ViewPager2LazyLoadFragment implements NewsFMvp.view, OnRefreshListener, OnLoadMoreListener {

    private static String TAG = "NewsFragment";
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
    @BindView(R.id.ll_news)
    LinearLayout llNews;
    private NewsFPresenter newsFPresenter = new NewsFPresenter(this);;
    private NewsRecyclerviewAdapter newsRecyclerviewAdapter;
    private NewsData bannerData;//轮播图数据
    private int page = 2;
    private boolean isLoadMore = false;

    public static NewsFragment getInstance(){
        return new NewsFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyLog.e(TAG, "NewsFragment：onCreateView");
        View view = inflater.inflate(R.layout.fragment_news, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        MyLog.e(TAG, "NewsFragment懒加载");
        init();
        getData();
    }

    public void init() {
        refresh.setOnRefreshListener(this);
        refresh.setOnLoadMoreListener(this);
        llNews.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);

        newsRecyclerviewAdapter = new NewsRecyclerviewAdapter(getContext());
        recyclerView.setAdapter(newsRecyclerviewAdapter);
        newsRecyclerviewAdapter.setOnClickListener(position -> {
            if (newsRecyclerviewAdapter.getData() != null){
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("Url", newsRecyclerviewAdapter.getData().get(position).getPath());
                startActivity(intent);
            }
        });

        //设置轮播图的点击事件
        bannerView.setBannerPageClickListener((view, position) -> {
            if (bannerData != null){
                Intent intent = new Intent(getContext(), WebActivity.class);
                intent.putExtra("Url", bannerData.getResult().get(position).getPath());
                startActivity(intent);
            }
        });
        //设置轮播图指示器
        bannerView.setIndicatorAlign(MZBannerView.IndicatorAlign.RIGHT);

        if (getContext() != null){
            //向RecyclerView添加分割线
            recyclerView.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));
        }
    }

    public void getData() {
        //获取轮播图新闻
        newsFPresenter.getNews("1", "7", false);
        //获取下方新闻，同一个接口同时请求时服务器会繁忙，第二个数据需要延迟请求
        Handler handler = new Handler();
        Runnable runnable = () -> newsFPresenter.getNews(String.valueOf(page), "7", isLoadMore);
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
        //设置轮播图内容
        bannerData = data;
        bannerView.setPages(data.getResult(), () -> new ViewPagerHolder());
    }

    @Override
    public void getNewsResult2(NewsData data, boolean isLoadMore) {
        if (isLoadMore) {
            newsRecyclerviewAdapter.addData(data.getResult());
        } else {
            newsRecyclerviewAdapter.setData(data.getResult());
        }
    }

    @Override
    public void onError(Throwable e, String type) {
        if ("getNews".equals(type)) {
            llNews.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
            refresh.finishLoadMore();
            refresh.finishRefresh();
        }
    }

    @Override
    public void onComplete(String type) {
        if ("getNews".equals(type)) {
            llNews.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
            refresh.finishLoadMore();
            refresh.finishRefresh();
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 2;
        isLoadMore = false;
        getData();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page += 1;
        //获取下方新闻
        isLoadMore = true;
        newsFPresenter.getNews(String.valueOf(page), "7", isLoadMore);
    }

    @OnClick({R.id.error_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                llNews.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                getData();
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
