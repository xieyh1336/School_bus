package com.example.school_bus.Activity;

import android.app.AlertDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;

import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Adapter.WaitingBusListAdapter;
import com.example.school_bus.Entity.BusListData;
import com.example.school_bus.Mvp.WaitingBusListMvp;
import com.example.school_bus.Presenter.WaitingBusListPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class WaitingBusListActivity extends BaseActivity implements WaitingBusListMvp.view {

    @BindView(R.id.rv_list)
    RecyclerView rvList;
    @BindView(R.id.loading_view)
    LinearLayout loadingView;
    @BindView(R.id.error_view)
    LinearLayout errorView;
    private WaitingBusListPresenter waitingBusListPresenter = new WaitingBusListPresenter(this);
    private WaitingBusListAdapter waitingBusListAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_waiting_bus_list);
        ButterKnife.bind(this);
        init();
        getData();
    }

    private void init() {
        Intent intent = getIntent();

        waitingBusListAdapter = new WaitingBusListAdapter();
        rvList.setAdapter(waitingBusListAdapter);
        rvList.addItemDecoration(new DividerItemDecoration(this, DividerItemDecoration.VERTICAL));//添加分割线
        waitingBusListAdapter.setOnClickListener((position, data) -> {
            AlertDialog.Builder builder = new AlertDialog.Builder(WaitingBusListActivity.this);
            builder.setMessage("确定要选取当前车辆发车吗？");
            builder.setPositiveButton("确定", (dialog, which) -> {
                intent.putExtra("id", data.getData().get(position).getId());
                setResult(DriverActivity.RUNNING, intent);
                finish();
            });
            builder.setNegativeButton("取消", null);
            builder.show();
        });
    }

    private void getData() {
        loadingView.setVisibility(View.VISIBLE);
        errorView.setVisibility(View.GONE);
        waitingBusListPresenter.getAllWaitingBus();
    }

    @OnClick({R.id.error_view})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.error_view:
                getData();
                loadingView.setVisibility(View.VISIBLE);
                errorView.setVisibility(View.GONE);
                break;
        }
    }

    @Override
    public void getAllWaitingBusResult(BusListData busListData) {
        if (busListData.isSuccess()) {
            waitingBusListAdapter.setData(busListData);
            loadingView.setVisibility(View.GONE);
            errorView.setVisibility(View.GONE);
        } else {
            loadingView.setVisibility(View.GONE);
            errorView.setVisibility(View.VISIBLE);
        }
    }

    @Override
    public void onError(Throwable e) {
        HttpUtil.onError(e);
        loadingView.setVisibility(View.GONE);
        errorView.setVisibility(View.VISIBLE);
    }
}