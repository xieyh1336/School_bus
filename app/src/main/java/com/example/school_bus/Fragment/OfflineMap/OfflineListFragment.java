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
import com.example.school_bus.Activity.StudentActivity;
import com.example.school_bus.Adapter.OfflineMap.OfflineList1Adapter;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Fragment.OfflineMainFragment;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-05 14:17
 * @类名 OfflineListFragment
 * @所在包 com\example\school_bus\Fragment\OfflineMap\OfflineListFragment.java
 * 离线地图，全部城市
 */
public class OfflineListFragment extends BaseVp2LazyLoadFragment {

    private static String TAG = "OfflineListFragment";
    @BindView(R.id.et_search)
    EditText etSearch;
    @BindView(R.id.rv_list)
    RecyclerView rvList;
    private OfflineList1Adapter offlineList1Adapter;
    private boolean isClick = false;//用于防止recyclerView实时刷新无法点击

    public static OfflineListFragment newInstance() {
        return new OfflineListFragment();
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_offline_map_list, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        offlineList1Adapter = new OfflineList1Adapter(getContext(), true);
        if (getActivity() != null){
            MKOfflineMap mkOfflineMap = ((StudentActivity) getActivity()).offlineMainFragment.getMkOfflineMap();

            MyLog.e(TAG, "加载所有离线地图列表数据");
            rvList.setAdapter(offlineList1Adapter);
            offlineList1Adapter.setData(mkOfflineMap);

            ((StudentActivity) getActivity()).offlineMainFragment.setOfflineListener(new OfflineMainFragment.OfflineListener() {
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
                        offlineList1Adapter.notifyDataSetChanged();
                    }
                }
            });
            //防止刷新和点击冲突
            offlineList1Adapter.setCallback(new OfflineList1Adapter.Callback() {
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
                                offlineList1Adapter.notifyDataSetChanged();
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
                if (etSearch.getText().toString().replaceAll(" ", "").length() > 0){
                    offlineList1Adapter.setIsSearch(true, etSearch.getText().toString().replaceAll(" ", ""));
                } else {
                    offlineList1Adapter.setIsSearch(false, null);
                }
            }

            @Override
            public void afterTextChanged(Editable s) {

            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        if (offlineList1Adapter != null){
            offlineList1Adapter.notifyData();
        }
    }
}