package com.example.school_bus.Fragment;

import android.Manifest;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import com.baidu.location.BDAbstractLocationListener;
import com.baidu.location.BDLocation;
import com.baidu.location.LocationClient;
import com.baidu.location.LocationClientOption;
import com.baidu.mapapi.CoordType;
import com.baidu.mapapi.SDKInitializer;
import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.BitmapDescriptor;
import com.baidu.mapapi.map.BitmapDescriptorFactory;
import com.baidu.mapapi.map.MapPoi;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.school_bus.R;
import com.example.school_bus.View.PagerDrawerPopupView;
import com.lxj.xpopup.XPopup;

import java.text.DecimalFormat;
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
    @BindView(R.id.ll_right)
    LinearLayout llRight;
    @BindView(R.id.tv_right_click_latitude)
    TextView tvRightClickLatitude;
    @BindView(R.id.tv_right_click_longitude)
    TextView tvRightClickLongitude;
    @BindView(R.id.tv_right_me_latitude)
    TextView tvRightMeLatitude;
    @BindView(R.id.tv_right_me_longitude)
    TextView tvRightMeLongitude;
    @BindView(R.id.iv_memu)
    ImageView ivMemu;
    @BindView(R.id.tv_right_click)
    TextView tvRightClick;
    @BindView(R.id.tv_right_me)
    TextView tvRightMe;
    private LocationClient locationClient;
    private BDLocation myLocation = new BDLocation();//我的位置
    private BDLocation clickLocation = new BDLocation();//点击位置
    private boolean isLock = false;//是否锁定我的位置
    private BaiduMap baiduMap;//定位图层
    private MapStatusUpdate mapStatusUpdate;
    private MyLocationListener myLocationListener;
    private MyLocationData locationData;
    private UiSettings uiSettings;
    private BitmapDescriptor bitmapDescriptor;//地图点击出现的图标
    private MarkerOptions markerOptions = new MarkerOptions();//用于在地图上添加Market
    private DecimalFormat decimalFormat = new DecimalFormat("#.00");//保留小数点后两位
    private PagerDrawerPopupView pagerDrawerPopupView;//侧边弹窗

    public static MapFragment getInstance() {
        if (mapFragment == null) {
            mapFragment = new MapFragment();
        }
        return mapFragment;
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(getActivity().getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        requestPermissions();
        initView();
        initMap(view);
        interactive();
        initData();
        return view;
    }

    public void initView() {
        pagerDrawerPopupView = new PagerDrawerPopupView(getContext());

        llRight.setVisibility(View.GONE);

        tvRightClick.setVisibility(View.GONE);
        tvRightClickLatitude.setVisibility(View.GONE);
        tvRightClickLongitude.setVisibility(View.GONE);
        tvRightClickLatitude.setText("0");
        tvRightClickLongitude.setText("0");
    }

    public void initMap(View view) {
        //定位初始化
        locationClient = new LocationClient(getContext());

        baiduMap = mapView.getMap();
        //开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);

        uiSettings = baiduMap.getUiSettings();

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
        locationClient.setLocOption(option);

        myLocationListener = new MyLocationListener();
        locationClient.registerLocationListener(myLocationListener);
        locationClient.start();
    }

    public void initData(){
        //接口回调
        pagerDrawerPopupView.setDataResult(show -> {
            if (show){
                llRight.setVisibility(View.VISIBLE);
            }else {
                llRight.setVisibility(View.GONE);
            }
        });
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
        }
    }

    //交互事件
    public void interactive() {
        //地图单击事件
        BaiduMap.OnMapClickListener onMapClickListener = new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             * @param latLng 点击的地理坐标
             */
            @Override
            public void onMapClick(LatLng latLng) {
                //添加之前把上一次点击的图标清空
                baiduMap.clear();
                //记录点击位置的经纬度
                clickLocation.setLatitude(latLng.latitude);
                clickLocation.setLongitude(latLng.longitude);

                //显示坐标
                tvRightClick.setVisibility(View.VISIBLE);
                tvRightClickLatitude.setVisibility(View.VISIBLE);
                tvRightClickLongitude.setVisibility(View.VISIBLE);
                tvRightClickLatitude.setText(decimalFormat.format(clickLocation.getLatitude()));
                tvRightClickLongitude.setText(decimalFormat.format(clickLocation.getLongitude()));

                //创建点击位图
                bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_click2);
                //构建MarkerOption，用于在地图上添加Market
                markerOptions.position(latLng);
                markerOptions.icon(bitmapDescriptor);
                OverlayOptions options = markerOptions;
                //在地图上添加Marker，并显示
                baiduMap.addOverlay(options);
            }

            /**
             * 地图内Poi单击事件回调函数
             * @param mapPoi 点击的Poi信息
             */
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        };
        baiduMap.setOnMapClickListener(onMapClickListener);
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

    @OnClick({R.id.iv_lock,R.id.iv_memu})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_lock:
                if (!isLock) {
                    //锁定状态
                    ivLock.setImageResource(R.mipmap.positioning_select);
                    //锁定状态不允许平移
                    uiSettings.setScrollGesturesEnabled(false);
                    //移动到我的位置
                    LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                    isLock = true;
                } else {
                    ivLock.setImageResource(R.mipmap.positioning_unselect);
                    //解锁后允许平移
                    uiSettings.setScrollGesturesEnabled(true);
                    isLock = false;
                }
                break;
            case R.id.iv_memu:
                new XPopup.Builder(getContext())
                        .isDestroyOnDismiss(true) //对于只使用一次的弹窗，推荐设置这个
//                        .hasShadowBg(false)
                        .asCustom(pagerDrawerPopupView)
                        .show();
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

    //获取定位数据
    public class MyLocationListener extends BDAbstractLocationListener {

        @Override
        public void onReceiveLocation(BDLocation bdLocation) {
            //mapView销毁后不再处理新接收的位置
            if (bdLocation == null || mapView == null) {
                return;
            }
            myLocation = bdLocation;
            locationData = new MyLocationData.Builder()
                    .accuracy(bdLocation.getRadius())
                    .direction(bdLocation.getDirection())
                    .latitude(bdLocation.getLatitude())
                    .longitude(bdLocation.getLongitude())
                    .build();
            baiduMap.setMyLocationData(locationData);

            tvRightMeLatitude.setText(decimalFormat.format(myLocation.getLatitude()));
            tvRightMeLongitude.setText(decimalFormat.format(myLocation.getLongitude()));

            if (isLock) {
                //移动到我的位置
                LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                baiduMap.animateMapStatus(mapStatusUpdate);
            }
        }
    }
}
