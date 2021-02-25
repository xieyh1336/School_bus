package com.example.school_bus.Fragment.Main.More;

import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Looper;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Adapter.More.PicturesRecyclerviewAdapter;
import com.example.school_bus.Fragment.FragmentOnKeyListener;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Mvp.PicturesMvp;
import com.example.school_bus.Presenter.PicturesPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.FileUtil;
import com.example.school_bus.Utils.MyLog;
import com.example.school_bus.Utils.PhotoLoader;
import com.example.school_bus.View.MyPopupWindow;
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
public class PicturesFragment extends BaseVp2LazyLoadFragment implements PicturesMvp.view, OnRefreshListener, OnLoadMoreListener, FragmentOnKeyListener {

    private static String TAG = "PicturesFragment";
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
    private PicturesPresenter picturesPresenter = new PicturesPresenter(this);
    private boolean isLoading = false;
    private PicturesRecyclerviewAdapter picturesRecyclerviewAdapter;
    private PhotoLoader loader = new PhotoLoader();
    private MyPopupWindow myPopupWindow;//自定义弹窗

    public static PicturesFragment getInstance(){
        return new PicturesFragment();
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        MyLog.e(TAG, "PicturesFragment：onCreateView");
        View view = inflater.inflate(R.layout.fragment_picture, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        MyLog.e(TAG, "PicturesFragment懒加载");
        init();
    }

    public void init() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        refresh.setOnLoadMoreListener(this);
        refresh.setOnRefreshListener(this);

        picturesRecyclerviewAdapter = new PicturesRecyclerviewAdapter(getContext());
        rv.setAdapter(picturesRecyclerviewAdapter);

        //长按弹出菜单
        iv.setOnItemLongPressListener((position, imageView) -> {
            if (myPopupWindow == null){
                myPopupWindow = new MyPopupWindow(getContext());
            }
            myPopupWindow.showPicture();
            myPopupWindow.setOnClickListener(type -> {
                switch (type){
                    case MyPopupWindow.SAVE_PICTURE:
                        //保存照片
                        Bitmap bitmap = FileUtil.drawableToBitmap(imageView.getDrawable());
                        Thread thread = new Thread(() -> {
                            boolean a = FileUtil.SaveBitmapFromView(bitmap, getContext());
                            Looper.prepare();
                            if (a) {
                                showToast("保存成功");
                            } else {
                                showToast("保存失败");
                            }
                            Looper.loop();
                        });
                        thread.start();
                        break;
                }
            });
            return true;
        });
        getData();
    }

    private void getData(){
        picturesPresenter.getPictures2(10, getContext());
    }

    @Override
    public void getPicturesResult2(List<Drawable> list) {
        if (isLoading){
            picturesRecyclerviewAdapter.addData(list);
        } else {
            picturesRecyclerviewAdapter.setData(list);
        }
        //大图加载器
        iv.overlayStatusBar(true)//是否会占据 StatusBar 的空间
                .imageData(picturesRecyclerviewAdapter.getData())
                .imageLoader(loader);
        picturesRecyclerviewAdapter.setOnClickListener(position -> {
            iv.bindViewGroup(rv);
            iv.watch(position);
        });

        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.GONE);
        refresh.finishLoadMore();
        refresh.finishRefresh();
    }

    @Override
    public void onLoadMore(@NonNull RefreshLayout refreshLayout) {
        isLoading = true;
        getData();
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        isLoading = false;
        getData();
    }

    @OnClick({R.id.error_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                getData();
                break;
        }
    }

    @Override
    public boolean onKeyDown(int keyCode, KeyEvent event) {
        //键盘监听
        MyLog.e("picture", "监听键盘成功，keyCode为：" + keyCode);
        return iv.onKeyDown(keyCode, event);
    }
}
