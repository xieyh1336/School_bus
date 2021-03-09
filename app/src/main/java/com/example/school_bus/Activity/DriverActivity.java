package com.example.school_bus.Activity;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.view.GravityCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.school_bus.Fragment.Main.MapFragment;
import com.example.school_bus.MyApp;
import com.example.school_bus.R;
import com.example.school_bus.Utils.MyLog;

import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

public class DriverActivity extends BaseActivity {

    private static String TAG = "DriverActivity";
    public final static int MAP_PERMISSION = 100;//地图权限申请码
    @BindView(R.id.tv_login_out)
    TextView tvLoginOut;
    @BindView(R.id.tv_run)
    TextView tvRun;
    @BindView(R.id.tv_arrive)
    TextView tvArrive;
    @BindView(R.id.tv_driver_name)
    TextView tvDriverName;
    @BindView(R.id.tv_longitude)
    TextView tvLongitude;
    @BindView(R.id.tv_latitude)
    TextView tvLatitude;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.mapView)
    MapView mapView;
    private BDLocation myLocation = new BDLocation();//监听，我的位置
    private BaiduMap baiduMap;//定位图层
    private LocationClient locationClient;//用于发起定位
    private UiSettings uiSettings;//UI设置
    private long secondBackTime;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(Objects.requireNonNull(getApplicationContext()));
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
        setContentView(R.layout.activity_driver);
        ButterKnife.bind(this);
        requestPermissions();
    }

    /**
     * 申请权限
     */
    public void requestPermissions() {
            if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                    ActivityCompat.requestPermissions(this, new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, MAP_PERMISSION);

            } else {
                init();
            }

    }

    private void init(){
        tvDriverName.setText(MyApp.getUserName());
        //定位初始化
        locationClient = new LocationClient(this);
        //地图的定位图层
        baiduMap = mapView.getMap();
        //开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);
        uiSettings = baiduMap.getUiSettings();
        uiSettings.setScrollGesturesEnabled(false);//禁止手势
        //服务基本设置
        LocationClientOption option = new LocationClientOption();
        option.setLocationMode(LocationClientOption.LocationMode.Hight_Accuracy);//可选，默认高精度，设置定位模式，高精度，低功耗，仅设备
        option.setCoorType("bd09ll");//可选，默认gcj02，设置返回的定位结果坐标系
        int span = 1000;
        option.setScanSpan(span);//可选，默认0，即仅定位一次，设置发起定位请求的间隔需要大于等于1000ms才是有效的
        option.setIsNeedAddress(true);//可选，设置是否需要地址信息，默认不需要
        option.setOpenGps(true);//可选，默认false,设置是否使用gps
        option.setLocationNotify(true);//可选，默认false，设置是否当gps有效时按照1S1次频率输出GPS结果
        option.setIsNeedLocationDescribe(true);//可选，默认false，设置是否需要位置语义化结果，可以在BDLocation.getLocationDescribe里得到，结果类似于“在北京天安门附近”
        option.setIsNeedLocationPoiList(true);//可选，默认false，设置是否需要POI结果，可以在BDLocation.getPoiList里得到
        option.setIgnoreKillProcess(false);//可选，默认true，定位SDK内部是一个SERVICE，并放到了独立进程，设置是否在stop的时候杀死这个进程，默认不杀死
        option.SetIgnoreCacheException(false);//可选，默认false，设置是否收集CRASH信息，默认收集
        option.setEnableSimulateGps(false);//可选，默认false，设置是否需要过滤gps仿真结果，默认需要
        option.setOpenAutoNotifyMode(3000, 1, LocationClientOption.LOC_SENSITIVITY_HIGHT);//只要定位发生变化就主动回调，用于连续定位
        locationClient.setLocOption(option);
        //注册监听
        MyLocationListener myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        locationClient.start();
    }

    private void notifyLocation(){
        tvLatitude.setText(String.valueOf(myLocation.getLatitude()));
        tvLongitude.setText(String.valueOf(myLocation.getLongitude()));

        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(myLocation.getRadius())
                .direction(myLocation.getDirection())
                .latitude(myLocation.getLatitude())
                .longitude(myLocation.getLongitude())
                .build();
        baiduMap.setMyLocationData(locationData);
        //直接缩放至缩放级别16
        baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
        //移动到我的位置
        LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
        MapStatusUpdate mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
        baiduMap.animateMapStatus(mapStatusUpdate);
    }

    @OnClick({R.id.tv_run, R.id.tv_arrive, R.id.tv_login_out})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.tv_run:
                break;
            case R.id.tv_arrive:
                break;
            case R.id.tv_login_out:
                MyApp.clearToken();
                startLogin();
                finish();
                break;
        }
    }

    @Override
    public void onBackPressed() {
        long firstBackTime = System.currentTimeMillis();
        if (firstBackTime - secondBackTime > 2000) {
            showToast("再按一次退出程序");
            secondBackTime = firstBackTime;
        } else {
            finish();
        }
    }

    @Override
    protected void onResume() {
        super.onResume();
        mapView.onResume();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (locationClient != null){
            locationClient.stop();
        }
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    /**
     * 权限申请回调
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        if (requestCode == MapFragment.MAP_PERMISSION) {
            if (grantResults.length > 0) {
                if (grantResults[0] == PackageManager.PERMISSION_GRANTED
                        && grantResults[1] == PackageManager.PERMISSION_GRANTED
                        && grantResults[2] == PackageManager.PERMISSION_GRANTED) {
                    MyLog.e(TAG, "所有权限已同意");
                    init();
                } else {
                    showToast("必须同意所有权限才能使用本程序!");
                }
            } else {
                showToast("发生未知错误");
            }
        }
    }

    //获取定位数据
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //mapView销毁后不再处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
            MyLog.e(TAG, "Listener纬度：" + bdLocation.getLatitude());
            MyLog.e(TAG, "Listener经度：" + bdLocation.getLongitude());
            myLocation = bdLocation;
            notifyLocation();//刷新数据
        }
    }
}