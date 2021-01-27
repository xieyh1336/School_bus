package com.example.school_bus.Fragment;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Adapter.PicturesRecyclerviewAdapter;
import com.example.school_bus.Mvp.PicturesFMvp;
import com.example.school_bus.Presenter.PicturesFPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;
import com.example.school_bus.Utils.PhotoLoader;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnLoadMoreListener;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;
import indi.liyi.viewer.ImageViewer;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 14:07
 * @类名 PicturesFragment
 * @所在包 com\example\school_bus\Fragment\PicturesFragment.java
 * 更多页面，图片分页
 */
public class PicturesFragment extends BaseFragment implements PicturesFMvp.view, OnRefreshListener, OnLoadMoreListener, FragmentOnKeyListener {

    @BindView(R.id.rv)
    RecyclerView rv;
    @BindView(R.id.iv)
    ImageViewer iv;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    private View view;
    private PicturesFPresenter picturesFPresenter;
    private int page = 3;
    private boolean isLoading = false;
    private PicturesRecyclerviewAdapter picturesRecyclerviewAdapter;
    private boolean loading = false;//用于判断是否在网络访问加载
    private PhotoLoader loader = new PhotoLoader();

    public static PicturesFragment getInstance(){
        return new PicturesFragment();
    }
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_picture, container, false);
        initView();
        initData();
        return view;
    }

    public void initView() {
        ButterKnife.bind(this, view);

        picturesFPresenter = new PicturesFPresenter(this);
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        refresh.setVisibility(View.GONE);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);
    }

    public void initData() {
        if (!loading){
            loading = true;
            picturesFPresenter.getPictures2(10, isLoading, getContext());
        }
    }

//    @Override
//    public void getPicturesResult(PicturesData data, boolean isLoadMore) {
//        if (picturesRecyclerviewAdapter != null) {
//            if (isLoadMore) {
//                picturesRecyclerviewAdapter.addData(data.getResult());
//            } else {
//                picturesRecyclerviewAdapter.setNewData(data.getResult());
//            }
//            picturesRecyclerviewAdapter.notifyDataSetChanged();
//            return;
//        }
//        picturesRecyclerviewAdapter = new PicturesRecyclerviewAdapter(R.layout.item_more_pictures_recyclerview, data.getResult(), getContext());
//        rv.setAdapter(picturesRecyclerviewAdapter);
//        picturesRecyclerviewAdapter.setOnItemClickListener(new PicturesRecyclerviewAdapter.OnItemClickListener() {
//            @Override
//            public void OnItemClick(int position) {
//
//            }
//        });
//    }

    @Override
    public void getPicturesResult2(List<Drawable> list, boolean isLoadMore) {
        loading = false;
        if (picturesRecyclerviewAdapter != null) {
            if (isLoadMore) {
                picturesRecyclerviewAdapter.addData(list);
            } else {
                picturesRecyclerviewAdapter.setNewData(list);
            }
            picturesRecyclerviewAdapter.notifyDataSetChanged();
            iv.overlayStatusBar(false)
                    .imageData(picturesRecyclerviewAdapter.getData())
                    .imageLoader(loader);
            errorView.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            refresh.setVisibility(View.VISIBLE);
            if (isLoading){
                refresh.finishLoadMore();
            }else {
                refresh.finishRefresh();
            }
            return;
        }
        iv.overlayStatusBar(false)
                .imageData(list)
                .imageLoader(loader);
        picturesRecyclerviewAdapter = new PicturesRecyclerviewAdapter(R.layout.item_more_pictures_recyclerview, list, getContext());
        rv.setAdapter(picturesRecyclerviewAdapter);
        picturesRecyclerviewAdapter.setOnItemClickListener(new PicturesRecyclerviewAdapter.OnItemClickListener() {
            @Override
            public void OnItemClick(int position) {
                iv.bindViewGroup(rv);
                iv.watch(position);
            }
        });
        picturesRecyclerviewAdapter.setNewData(list);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        refresh.setVisibility(View.VISIBLE);
        if (isLoading){
            refresh.finishLoadMore();
        }else {
            refresh.finishRefresh();
        }
    }

    @Override
    public void onError(Throwable e, String type) {
        errorView.setVisibility(View.VISIBLE);
        loadingView.setVisibility(View.GONE);
    }

    @Override
    public void onComplete(String type) {
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        if (isLoading){
            refresh.finishLoadMore();
        }else {
            refresh.finishRefresh();
        }
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        page += 1;
        isLoading = true;
        picturesFPresenter.getPictures2(10, isLoading, getContext());
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        page = 1;
        isLoading = false;
        initData();
    }

    @OnClick({R.id.error_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                initData();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //键盘监听
        MyLog.e("picture", "监听键盘成功，keyCode为：" + keyCode);
        boolean b = iv.onKeyDown(keyCode, event);
        if (b){
            return b;
        }
        return false;
    }
}
