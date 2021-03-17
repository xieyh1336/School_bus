package com.example.school_bus.Activity;

import android.app.AlertDialog;
import android.os.Bundle;
import android.os.Handler;
import android.view.View;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Adapter.AllOrderAdapter;
import com.example.school_bus.Entity.AllOrderData;
import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.MyStateData;
import com.example.school_bus.Mvp.MyOrderMvp;
import com.example.school_bus.Presenter.MyOrderPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;
import com.scwang.smartrefresh.layout.SmartRefreshLayout;
import com.scwang.smartrefresh.layout.api.RefreshLayout;
import com.scwang.smartrefresh.layout.listener.OnRefreshListener;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-03-13 21:43
 * @类名 MyOrderActivity
 * @所在包 com\example\school_bus\Activity\MyOrderActivity.java
 * 我的订单
 */
public class MyOrderActivity extends BaseActivity implements OnRefreshListener, MyOrderMvp.view {

    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_id)
    TextView tvId;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_plates)
    TextView tvPlates;
    @BindView(R.id.tv_time)
    TextView tvTime;
    @BindView(R.id.tv_cancel)
    TextView tvCancel;
    @BindView(R.id.ll_now_order)
    LinearLayout llNowOrder;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.refresh)
    SmartRefreshLayout refresh;
    @BindView(R.id.tv_tip)
    TextView tvTip;
    private MyOrderPresenter myOrderPresenter = new MyOrderPresenter(this);
    private AllOrderAdapter allOrderAdapter = new AllOrderAdapter();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_order);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        refresh.setOnRefreshListener(this);
        tvTip.setVisibility(View.GONE);
        rvList.setAdapter(allOrderAdapter);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//添加分割线
        getData();
    }

    private void getData() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        myOrderPresenter.getMyState();
    }

    @OnClick({R.id.error_view, R.id.tv_cancel})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                getData();
                break;
            case R.id.tv_cancel:
                AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setMessage("确定取消当前订单吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    myOrderPresenter.cancelOrder();
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
        }
    }

    @Override
    public void onRefresh(@NonNull RefreshLayout refreshLayout) {
        myOrderPresenter.getMyState();
    }

    @Override
    public void getMyStateResult(MyStateData myStateData) {
        switch (myStateData.getData().getState()) {
            case "0":
                llNowOrder.setVisibility(View.GONE);
                tvState.setText("未预约");
                break;
            case "1":
                tvState.setText("已预约");
                llNowOrder.setVisibility(View.VISIBLE);
                tvId.setText(myStateData.getData().getBus_id());
                tvName.setText(myStateData.getData().getBus_name());
                tvPlates.setText(myStateData.getData().getPlates());
                tvTime.setText(myStateData.getData().getRun_time());
                break;
            case "2":
                tvState.setText("行驶中");
                llNowOrder.setVisibility(View.VISIBLE);
                tvId.setText(myStateData.getData().getBus_id());
                tvName.setText(myStateData.getData().getBus_name());
                tvPlates.setText(myStateData.getData().getPlates());
                tvTime.setText(myStateData.getData().getRun_time());
                break;
        }
        new Handler().postDelayed(() -> myOrderPresenter.getAllOrder(), 1000);
    }

    @Override
    public void getAllOrderResult(AllOrderData allOrderData) {
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.GONE);
        allOrderAdapter.setData(allOrderData);
        if (allOrderData.getData().size() == 0){
            tvTip.setVisibility(View.VISIBLE);
        } else {
            tvTip.setVisibility(View.GONE);
        }
        refresh.finishRefresh();
    }

    @Override
    public void cancelOrderResult(BaseData baseData) {
        showToast("取消成功");
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        new Handler().postDelayed(this::getData, 1000);
    }

    @Override
    public void onError(Throwable e, String type) {
        switch (type) {
            case "getMyState":
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.VISIBLE);
                HttpUtil.onError(e);
                refresh.finishRefresh();
                break;
            case "getAllOrder":
                loadingView.setVisibility(View.GONE);
                errorView.setVisibility(View.GONE);
                refresh.finishRefresh();
                break;
            case "cancelOrder":
                HttpUtil.onError(e);
                break;
        }
    }
}