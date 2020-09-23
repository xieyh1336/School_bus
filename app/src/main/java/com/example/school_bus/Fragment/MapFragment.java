package com.example.school_bus.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.model.LatLng;
import com.example.school_bus.R;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * 地图界面
 */
public class MapFragment extends BaseFragment {
    private static MapFragment mapFragment;
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv_lock)
    ImageView ivLock;
    private LocationClient locationClient;
    private BDLocation myLocation;//我的位置
    private double nowLatitude;//当前纬度
    private double nowLongitude;//当前经度
    private boolean isLock = false;//是否锁定我的位置
    private BaiduMap baiduMap;
    private MapStatusUpdate mapStatusUpdate;
    private MyLocationData.Builder builder;
    private MyLocationData locationData;

    public static MapFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        return mapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        SDKInitializer.initialize(getActivity().getApplicationContext());
        locationClient = new LocationClient(getContext());
        locationClient.registerLocationListener(new MyLocationListener());
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        initView(view);
        requestPermissions();
        return view;
    }

    public void initView(View view) {
        ButterKnife.bind(this, view);
        baiduMap = mapView.getMap();
        baiduMap.setMyLocationEnabled(true);
    }

    //申请权限
    public void requestPermissions() {
        List<String> permissionList = new ArrayList<>();
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.ACCESS_FINE_LOCATION);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.READ_PHONE_STATE);
        }
        if (ContextCompat.checkSelfPermission(getContext(), Manifest.
                permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            permissionList.add(Manifest.permission.WRITE_EXTERNAL_STORAGE);
        }
        if (!permissionList.isEmpty()) {
            String[] permissions = permissionList.toArray(new String[permissionList.size()]);
            ActivityCompat.requestPermissions(getActivity(), permissions, 1);
        } else {
            locationClient.start();
        }
    }

    private void navigateTo(BDLocation location){
        LatLng latLng = new LatLng(location.getLatitude(),location.getLongitude());
        mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
        mapStatusUpdate = MapStatusUpdateFactory.zoomTo(16f);
        baiduMap.animateMapStatus(mapStatusUpdate);
        if (builder==null){
            builder = new MyLocationData.Builder();
        }
        builder.latitude(location.getLatitude());
        builder.longitude(location.getLongitude());
        locationData = builder.build();
        baiduMap.setMyLocationData(locationData);
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case 1:
                if (grantResults.length > 0) {
                    for (int result : grantResults) {
                        if (result != PackageManager.PERMISSION_GRANTED) {
                            showToast("必须同意所有权限才能使用本程序!");
                            return;
                        }
                    }
                    locationClient.start();
                } else {
                    showToast("发生未知错误");
                }
                break;
            default:
        }
    }

    @OnClick({ R.id.iv_lock})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_lock:
                if (isLock == false){
                    //锁定状态
                    ivLock.setImageResource(R.mipmap.positioning_select);
                    isLock = true;
                }else {
                    ivLock.setImageResource(R.mipmap.positioning_unselect);
                    isLock = false;
                }
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        locationClient.stop();
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            myLocation=bdLocation;
            Handler handler=new Handler();
            Runnable runnable = new Runnable() {
                @Override
                public void run() {
                    navigateTo(myLocation);
                    handler.postDelayed(this,5000);
                }
            };
            handler.post(runnable);
        }
    }
}
