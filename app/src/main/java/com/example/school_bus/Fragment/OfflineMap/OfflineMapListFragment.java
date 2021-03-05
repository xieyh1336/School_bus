package com.example.school_bus.Fragment.OfflineMap;

import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.example.school_bus.Activity.MainActivity;
import com.example.school_bus.Adapter.OfflineMap.OfflineMapList1Adapter;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Fragment.OfflineMapFragment;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-05 14:17
 * @类名 OfflineMapListFragment
 * @所在包 com\example\school_bus\Fragment\OfflineMap\OfflineMapListFragment.java
 * 离线地图，全部城市
 */
public class OfflineMapListFragment extends BaseVp2LazyLoadFragment {

    private static String TAG = "OfflineMapListFragment";
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private boolean isClick = false;//用于防止recyclerView实时刷新无法点击

    public static OfflineMapListFragment newInstance() {
        return new OfflineMapListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_map_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        OfflineMapList1Adapter offlineMapList1Adapter = new OfflineMapList1Adapter(getContext());
        if (getActivity() != null){
            MKOfflineMap mkOfflineMap = ((MainActivity) getActivity()).offlineMapFragment.getMkOfflineMap();

            MyLog.e(TAG, "加载所有离线地图列表数据");
            rvList.setAdapter(offlineMapList1Adapter);
            offlineMapList1Adapter.setData(mkOfflineMap);

            ((MainActivity) getActivity()).offlineMapFragment.setOfflineListener(new OfflineMapFragment.OfflineListener() {
                @Override
                public void mapListener(int type, int state) {
                    // 更新过程中的回调进度，可查看更新进度、新离线地图安装、版本更新提示。
                    // type - 事件类型:
                    // MKOfflineMap.TYPE_NEW_OFFLINE,
                    // MKOfflineMap.TYPE_DOWNLOAD_UPDATE,
                    // MKOfflineMap.TYPE_VER_UPDATE.
                    // state - 事件状态:
                    // 当type为TYPE_NEW_OFFLINE时，表示新安装的离线地图数目.
                    // 当type为TYPE_DOWNLOAD_UPDATE时，表示更新的城市ID.
                    if (!isClick){
                        offlineMapList1Adapter.notifyDataSetChanged();
                    }
                }
            });
            //防止刷新和点击冲突
            offlineMapList1Adapter.setCallback(new OfflineMapList1Adapter.Callback() {
                @Override
                public boolean rv2Listener(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    MyLog.e(TAG, "onInterceptTouchEvent");
                    switch (e.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            MyLog.e(TAG, "DOWN");
                            isClick = true;
                            new Handler().postDelayed(() -> {
                                isClick = false;
                            }, 500);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            MyLog.e(TAG, "MOVE");
                            break;
                        case MotionEvent.ACTION_UP:
                            MyLog.e(TAG, "UP");
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            MyLog.e(TAG, "CANCEL");
                            break;
                    }
                    return false;
                }

                @Override
                public void scrollPosition(int position) {
                    MyLog.e(TAG, "滑动到当前位置");
                    rvList.scrollToPosition(position);
                }
            });
            //防止刷新和点击冲突
            rvList.addOnItemTouchListener(new RecyclerView.OnItemTouchListener() {
                @Override
                public boolean onInterceptTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {
                    MyLog.e(TAG, "onInterceptTouchEvent");
                    switch (e.getAction()){
                        case MotionEvent.ACTION_DOWN:
                            MyLog.e(TAG, "DOWN");
                            isClick = true;
                            new Handler().postDelayed(() -> {
                                isClick = false;
                                offlineMapList1Adapter.notifyDataSetChanged();
                            }, 500);
                            break;
                        case MotionEvent.ACTION_MOVE:
                            MyLog.e(TAG, "MOVE");
                            break;
                        case MotionEvent.ACTION_UP:
                            MyLog.e(TAG, "UP");
                            break;
                        case MotionEvent.ACTION_CANCEL:
                            MyLog.e(TAG, "CANCEL");
                            break;
                    }
                    return false;
                }

                @Override
                public void onTouchEvent(@NonNull RecyclerView rv, @NonNull MotionEvent e) {

                }

                @Override
                public void onRequestDisallowInterceptTouchEvent(boolean disallowIntercept) {

                }
            });
        }
        etSearch.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                MyLog.e(TAG, "输入长度：" + etSearch.getText().toString().replaceAll(" ", "").length());
                if (etSearch.getText().toString().replaceAll(" ", "").length() > 0){
                    offlineMapList1Adapter.setIsSearch(true, etSearch.getText().toString().replaceAll(" ", ""));
                } else {
                    offlineMapList1Adapter.setIsSearch(false, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }
}