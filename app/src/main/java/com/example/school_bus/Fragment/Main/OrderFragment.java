package com.example.school_bus.Fragment.Main;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Activity.MyOrderActivity;
import com.example.school_bus.Adapter.WaitingBusListAdapter;
import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.BusListData;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Mvp.OrderMvp;
import com.example.school_bus.Presenter.OrderPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 预约界面
 */
public class OrderFragment extends BaseVp2LazyLoadFragment implements OnRefreshListener, OrderMvp.view {

    private static String TAG = "OrderFragment";
    @BindView(R.id.tv_top)
    TextView tvTop;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    private WaitingBusListAdapter waitingBusListAdapter = new WaitingBusListAdapter();
    private OrderPresenter orderPresenter = new OrderPresenter(this);

    public static OrderFragment newInstance() {
        return new OrderFragment();
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_order, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        waitingBusListAdapter = new WaitingBusListAdapter();
        rvList.setAdapter(waitingBusListAdapter);
        if (getContext() != null){
            rvList.addItemDecoration(new DividerItemDecoration(getContext(), DividerItemDecoration.VERTICAL));//添加分割线
            waitingBusListAdapter.setOnClickListener((position, data) -> {
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确定预约当前车辆吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    orderPresenter.orderBus(data.getData().get(position).getId());//预约校车
                });
                builder.setNegativeButton("取消", null);
                builder.show();
            });
        }
        refresh.setOnRefreshListener(this);
        errorView.setVisibility(View.GONE);
        loadingView.setVisibility(View.VISIBLE);
        orderPresenter.getAllWaitingBus();//获取校车列表
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        orderPresenter.getAllWaitingBus();//获取校车列表
    }

    @Override
    public void getAllWaitingBusResult(BusListData busListData) {
        refresh.finishRefresh();
        if (busListData.isSuccess()){
            errorView.setVisibility(View.GONE);
            loadingView.setVisibility(View.GONE);
            waitingBusListAdapter.setData(busListData);
        } else {
            errorView.setVisibility(View.VISIBLE);
            loadingView.setVisibility(View.GONE);
        }
    }

    @Override
    public void orderBusResult(BaseData baseData) {
        showToast("预约成功");
    }

    @Override
    public void onError(Throwable e, String type) {
        switch (type) {
            case "getAllWaitingBus":
                errorView.setVisibility(View.VISIBLE);
                loadingView.setVisibility(View.GONE);
                HttpUtil.onError(e);
                break;
            case "orderBus":
                HttpUtil.onError(e);
                break;
        }
    }

    @OnClick({R.id.error_view, R.id.tv_top})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                errorView.setVisibility(View.GONE);
                loadingView.setVisibility(View.VISIBLE);
                orderPresenter.getAllWaitingBus();//获取校车列表
                break;
            case R.id.tv_top:
                startActivity(new Intent(getContext(), MyOrderActivity.class));
                break;
        }
    }
}
