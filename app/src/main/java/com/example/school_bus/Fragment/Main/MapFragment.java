package com.example.school_bus.Fragment.Main;

import android.Manifest;
import android.app.AlertDialog;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CompoundButton;
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
import com.baidu.mapapi.map.MapStatus;
import com.baidu.mapapi.map.MapStatusUpdate;
import com.baidu.mapapi.map.MapStatusUpdateFactory;
import com.baidu.mapapi.map.MapView;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.MyLocationData;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.map.PolylineOptions;
import com.baidu.mapapi.map.UiSettings;
import com.baidu.mapapi.model.LatLng;
import com.example.school_bus.Entity.BaseData;
import com.example.school_bus.Entity.MapData;
import com.example.school_bus.Entity.MyStateData;
import com.example.school_bus.Entity.TestBus;
import com.example.school_bus.Fragment.LazyLoad.BaseVp2LazyLoadFragment;
import com.example.school_bus.Manage.MapDrawManage;
import com.example.school_bus.Mvp.MapMvp;
import com.example.school_bus.Presenter.MapPresenter;
import com.example.school_bus.R;
import com.example.school_bus.Utils.HttpUtil;
import com.example.school_bus.Utils.MyLog;
import com.example.school_bus.View.MyPopupWindow;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-27 14:07
 * @类名 MapFragment
 * @所在包 com\example\school_bus\Fragment\MapFragment.java
 * 地图页面主页
 */
public class MapFragment extends BaseVp2LazyLoadFragment implements MapMvp.view {
    private static String TAG = "MapFragment";
    public final static int MAP_PERMISSION = 100;//地图权限申请码
    @BindView(R.id.mapView)
    MapView mapView;
    @BindView(R.id.iv_my_location)
    ImageView ivMyLocation;
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
    @BindView(R.id.ic_menu)
    ImageView ivMenu;
    @BindView(R.id.tv_right_click)
    TextView tvRightClick;
    @BindView(R.id.tv_right_me)
    TextView tvRightMe;
    @BindView(R.id.tv_up)
    TextView tvUp;
    @BindView(R.id.tv_state)
    TextView tvState;
    @BindView(R.id.tv_on_bus)
    TextView tvOnBus;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_bus_id)
    TextView tvBusId;
    @BindView(R.id.tv_plates)
    TextView tvPlates;
    @BindView(R.id.tv_run_time)
    TextView tvRunTime;
    @BindView(R.id.ll_bus_information)
    LinearLayout llBusInformation;
    @BindView(R.id.tv_up_bus)
    TextView tvUpBus;
    @BindView(R.id.tv_down_bus)
    TextView tvDownBus;
    private LocationClient locationClient;//用于发起定位
    private BDLocation myLocation = new BDLocation();//监听，我的位置
    private BDLocation clickLocation = new BDLocation();//点击位置
    private boolean isLock = false;//是否锁定我的位置
    private BaiduMap baiduMap;//定位图层
    private MapStatusUpdate mapStatusUpdate;
    private UiSettings uiSettings;//UI设置
    private boolean isFirst = true;//是否第一次进入app
    private boolean isLoading = true;//地图是否正在加载
    private MyPopupWindow myPopupWindow;//功能区
    private boolean switch1 = false, switch2 = false, switch3 = false;//功能区选择框
    private MapDrawManage mapDrawManage;//绘制管理类
    private MarkerOptions markerOptions = new MarkerOptions();//绘制点击点
    private OverlayOptions testBus;//绘制
    private double clickLatitude, clickLongitude;
    private MapPresenter mapPresenter = new MapPresenter(this);

    public static MapFragment newInstance() {
        return new MapFragment();
    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //在使用SDK各组件之前初始化context信息，传入ApplicationContext
        SDKInitializer.initialize(Objects.requireNonNull(getActivity()).getApplicationContext());
        //自4.3.0起，百度地图SDK所有接口均支持百度坐标和国测局坐标，用此方法设置使用的坐标类型.
        //包括BD09LL和GCJ02两种坐标，默认是BD09LL坐标。
        SDKInitializer.setCoordType(CoordType.BD09LL);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_map, container, false);
        ButterKnife.bind(this, view);
        return view;
    }

    @Override
    public void lazyLoad() {
        isLoading = true;

        llRight.setVisibility(View.GONE);
        tvRightClick.setVisibility(View.GONE);
        tvRightClickLatitude.setVisibility(View.GONE);
        tvRightClickLongitude.setVisibility(View.GONE);
        llBusInformation.setVisibility(View.GONE);
        tvUpBus.setVisibility(View.GONE);
        tvDownBus.setVisibility(View.GONE);
        tvRightClickLatitude.setText("0");
        tvRightClickLongitude.setText("0");

        myPopupWindow = new MyPopupWindow(getContext());
        mapPresenter.testGetBus();//测试获取校车路线

        requestPermissions();//申请权限
    }

    /**
     * 申请权限
     */
    public void requestPermissions() {
        if (getContext() != null) {
            if (ContextCompat.checkSelfPermission(getContext(), Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.READ_PHONE_STATE) != PackageManager.PERMISSION_GRANTED
                    || ContextCompat.checkSelfPermission(getContext(), Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
                //申请权限
                if (getActivity() != null) {
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.ACCESS_FINE_LOCATION,
                            Manifest.permission.READ_PHONE_STATE,
                            Manifest.permission.WRITE_EXTERNAL_STORAGE
                    }, MAP_PERMISSION);
                }
            } else {
                init();
            }
        }
    }

    public void init() {
        //地图有关的初始化
        initMap();
        mapListener();
        getMyState();
        //监听
        setListener();
        isLoading = false;
        mapDrawManage = new MapDrawManage(baiduMap, markerOptions);
    }

    /**
     * 地图初始化
     */
    public void initMap() {
        //定位初始化
        locationClient = new LocationClient(getContext());
        //地图的定位图层
        baiduMap = mapView.getMap();
        //开启地图的定位图层
        baiduMap.setMyLocationEnabled(true);
        //UI设置
        uiSettings = baiduMap.getUiSettings();
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

        //创建点击位图
        BitmapDescriptor bitmapDescriptor = BitmapDescriptorFactory.fromResource(R.mipmap.ic_map_click2);//地图点击出现的图标
        markerOptions.icon(bitmapDescriptor);
    }

    /**
     * 地图监听
     */
    public void mapListener() {
        //地图加载完成
        baiduMap.setOnMapLoadedCallback(() -> {
            //地图加载完成
            MyLog.e(TAG, "地图加载完成");
        });
        //地图渲染完成回调
        baiduMap.setOnMapRenderCallbadk(() -> {
            //每次对地图进行操作的时候都会渲染
            MyLog.e(TAG, "地图渲染完成");
        });
        //地图单击事件
        baiduMap.setOnMapClickListener(new BaiduMap.OnMapClickListener() {
            /**
             * 地图单击事件回调函数
             * @param latLng 点击的地理坐标
             */
            @Override
            public void onMapClick(LatLng latLng) {
                MyLog.e(TAG, "-------------------------");
                MyLog.e(TAG, "点击了地图：");
                MyLog.e(TAG, "纬度：" + latLng.latitude);
                MyLog.e(TAG, "经度：" + latLng.longitude);
                //记录点击位置
                clickLatitude = latLng.latitude;
                clickLongitude = latLng.longitude;

                //记录点击位置的经纬度
                clickLocation.setLatitude(latLng.latitude);
                clickLocation.setLongitude(latLng.longitude);

                //显示坐标
                tvRightClick.setVisibility(View.VISIBLE);
                tvRightClickLatitude.setVisibility(View.VISIBLE);
                tvRightClickLongitude.setVisibility(View.VISIBLE);
                tvRightClickLatitude.setText(String.format(Locale.getDefault(), "%.6f", clickLocation.getLatitude()));
                tvRightClickLongitude.setText(String.format(Locale.getDefault(), "%.6f", clickLocation.getLongitude()));

                //如果开启了绘制点视图功能
                if (switch2) {
                    mapDrawManage.updateClick(latLng);
//                    //添加之前把上一次点击的图标清空
//                    baiduMap.clear();
//                    if (switch3) {
//                        baiduMap.addOverlay(testBus);
//                    }
//                    //构建MarkerOption，用于在地图上添加Market
//                    markerOptions.position(latLng);
//                    options = markerOptions;
//                    //在地图上添加Marker，并显示
//                    baiduMap.addOverlay(options);
                }
            }

            /**
             * 地图内Poi单击事件回调函数
             * @param mapPoi 点击的Poi信息
             */
            @Override
            public void onMapPoiClick(MapPoi mapPoi) {

            }
        });
        //地图状态监听
        baiduMap.setOnMapStatusChangeListener(new BaiduMap.OnMapStatusChangeListener() {
            /**
             * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
             * @param mapStatus 地图状态改变开始时的地图状态
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus) {

            }

            /**
             * 手势操作地图，设置地图状态等操作导致地图状态开始改变。
             * @param mapStatus 地图状态改变开始时的地图状态
             * @param reason 地图状态改变的原因
             *               用户手势触发导致的地图状态改变,比如双击、拖拽、滑动底图
             *               int REASON_GESTURE = 1;
             *               SDK导致的地图状态改变, 比如点击缩放控件、指南针图标
             *               int REASON_API_ANIMATION = 2;
             *               开发者调用,导致的地图状态改变
             *               int REASON_DEVELOPER_ANIMATION = 3;
             */
            @Override
            public void onMapStatusChangeStart(MapStatus mapStatus, int reason) {
                switch (reason) {
                    case REASON_GESTURE:
                        //用户手势触发导致地图状态改变
                        MyLog.e(TAG, "用户手势触发地图状态改变，mapStatus：" + mapStatus);
                        //地图状态改变，图标解锁
                        isLock = false;
                        ivMyLocation.setImageResource(R.mipmap.positioning_un_select);
                        break;
                    case REASON_API_ANIMATION:
                        //SDK导致地图状态改变
                        MyLog.e(TAG, "SDK导致地图状态改变，mapStatus：" + mapStatus);
                        break;
                    case REASON_DEVELOPER_ANIMATION:
                        //开发者调用，导致地图状态改变
                        MyLog.e(TAG, "开发者调用导致地图状态改变，mapStatus：" + mapStatus);
                        break;
                }
            }

            /**
             * 地图状态变化中
             * @param mapStatus 当前地图状态
             */
            @Override
            public void onMapStatusChange(MapStatus mapStatus) {

            }

            /**
             * 地图状态改变结束
             * @param mapStatus 地图状态改变结束后的地图状态
             */
            @Override
            public void onMapStatusChangeFinish(MapStatus mapStatus) {
                MyLog.e(TAG, "地图状态改变结束，mapStatus：" + mapStatus);
            }
        });
        //地图双击事件
        baiduMap.setOnMapDoubleClickListener(latLng -> {
            //双击了地图
            MyLog.e(TAG, "双击了地图");
        });
        //地图长按事件
        baiduMap.setOnMapLongClickListener(latLng -> {
            //长按了地图
            MyLog.e(TAG, "长按了地图");
        });
        //地图Marker覆盖物点击事件
        baiduMap.setOnMarkerClickListener(marker -> {
            //点击了Marker覆盖物
            MyLog.e(TAG, "点击了Marker覆盖物");
            return false;//是否捕获点击事件，不捕捉则OnMapClickListener捕捉
        });
        //地图触摸事件
        baiduMap.setOnMapTouchListener(motionEvent -> {
            //触摸了地图
            MyLog.e(TAG, "触摸了地图");
        });
        //地图截屏事件
        baiduMap.snapshot(bitmap -> {
            //地图截屏事件
            MyLog.e(TAG, "地图截屏事件");
        });
    }

    private void getMyState() {
        mapPresenter.getMyState();
    }

    private void setListener() {
        myPopupWindow.setOnSwitchListener(new MyPopupWindow.OnSwitchListener() {
            @Override
            public void onSwitch(CompoundButton buttonView, int num, boolean isSelect) {
                switch (num) {
                    case 1:
                        //是否显示坐标
                        switch1 = isSelect;
                        if (isSelect) {
                            llRight.setVisibility(View.VISIBLE);
                        } else {
                            llRight.setVisibility(View.GONE);
                        }
                        break;
                    case 2:
                        //是否绘制点视图
                        switch2 = isSelect;
                        if (isSelect) {
                            mapDrawManage.showClick();
                        } else {
                            mapDrawManage.hideClick();
                        }
//                        if (!isSelect) {
//                            baiduMap.clear();//清空视图
//                            if (switch3) {
//                                baiduMap.addOverlay(testBus);
//                            }
//                        }
                        break;
                    case 3:
                        //测试：是否绘制校车轨迹
                        MyLog.e(TAG, "测试" + testBus);
                        if (testBus != null) {
                            if (isSelect) {
                                mapDrawManage.add(testBus);
                            } else {
                                mapDrawManage.clear(testBus);
                            }
//                            if (isSelect) {
//                                MyLog.e(TAG, "绘制校车路线");
//                                baiduMap.addOverlay(testBus);
//                            } else {
//                                baiduMap.clear();
//                                if (switch2) {
//                                    baiduMap.addOverlay(options);
//                                }
//                            }
                        }
                        switch3 = isSelect;
                        break;
                }
            }
        });
    }

    /**
     * 刷新数据
     */
    private void notifyLocation() {
        //上传位置信息
        mapPresenter.upLocation(myLocation.getLatitude(), myLocation.getLongitude());
        //获取我的信息
        new Handler().postDelayed(this::getMyState, 1000);
        MyLog.e(TAG, "当前地址：" + myLocation.getAddrStr());
        MyLog.e(TAG, "当前国家：" + myLocation.getCountry());
        MyLog.e(TAG, "当前省份：" + myLocation.getProvince());
        MyLog.e(TAG, "当前城市：" + myLocation.getCity());
        MyLog.e(TAG, "当前区县：" + myLocation.getDistrict());
        MyLog.e(TAG, "当前街道信息：" + myLocation.getStreet());
        MyLog.e(TAG, "adcode：" + myLocation.getAdCode());
        MyLog.e(TAG, "当前乡镇信息：" + myLocation.getTown());
        MapData.AddrStr = myLocation.getAddrStr();
        MapData.Country = myLocation.getCountry();
        MapData.Province = myLocation.getProvince();
        MapData.City = myLocation.getCity();
        MapData.District = myLocation.getDistrict();
        MapData.Street = myLocation.getStreet();
        MapData.AdCode = myLocation.getAdCode();
        MapData.Town = myLocation.getTown();

        MyLocationData locationData = new MyLocationData.Builder()
                .accuracy(myLocation.getRadius())
                .direction(myLocation.getDirection())
                .latitude(myLocation.getLatitude())
                .longitude(myLocation.getLongitude())
                .build();
        baiduMap.setMyLocationData(locationData);
        tvRightMeLatitude.setText(String.format(Locale.getDefault(), "%.6f", myLocation.getLatitude()));
        tvRightMeLongitude.setText(String.format(Locale.getDefault(), "%.6f", myLocation.getLongitude()));

        if (isLock || isFirst) {
            isFirst = false;
            //直接缩放至缩放级别16
            baiduMap.setMapStatus(MapStatusUpdateFactory.zoomTo(16));
            //移动到我的位置
            LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
            mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
            baiduMap.animateMapStatus(mapStatusUpdate);
        }
    }

    @OnClick({R.id.iv_my_location, R.id.ic_menu, R.id.tv_up, R.id.tv_up_bus, R.id.tv_down_bus})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_my_location:
                if (!isLoading) {
                    //移动到我的位置
                    LatLng latLng = new LatLng(myLocation.getLatitude(), myLocation.getLongitude());
                    mapStatusUpdate = MapStatusUpdateFactory.newLatLng(latLng);
                    baiduMap.animateMapStatus(mapStatusUpdate);
                    if (!isLock) {
                        //锁定状态
                        ivMyLocation.setImageResource(R.mipmap.positioning_select);
                        isLock = true;
                    } else {
                        ivMyLocation.setImageResource(R.mipmap.positioning_un_select);
                        isLock = false;
                    }
                } else {
                    showToast("地图正在加载");
                }
                break;
            case R.id.ic_menu:
                if (myPopupWindow != null) {
                    if (myPopupWindow.getPopup() == null) {
                        myPopupWindow.showMapFunction(ivMenu);
                    } else {
                        if (myPopupWindow.getPopup().isShowing()) {
                            myPopupWindow.hide();
                        } else {
                            myPopupWindow.showMapFunction(ivMenu);
                        }
                    }
                }
                break;
            case R.id.tv_up:
                //上传路线
                mapPresenter.testInsertBus(clickLatitude, clickLongitude);
                showToast("上传成功");
                break;
            case R.id.tv_up_bus:
                //上车
                AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                builder.setMessage("确定上车吗？");
                builder.setPositiveButton("确定", (dialog, which) -> {
                    mapPresenter.upBus();
                });
                builder.setNegativeButton("取消", null);
                builder.show();
                break;
            case R.id.tv_down_bus:
                //下车
                AlertDialog.Builder builder2 = new AlertDialog.Builder(getContext());
                builder2.setMessage("确定下车吗？");
                builder2.setPositiveButton("确定", (dialog, which) -> {
                    mapPresenter.downBus();
                });
                builder2.setNegativeButton("取消", null);
                builder2.show();
                break;
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mapView.onResume();
        getMyState();
    }

    @Override
    public void onPause() {
        super.onPause();
        mapView.onPause();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (locationClient != null) {
            locationClient.stop();
        }
        mapView.onDestroy();
        baiduMap.setMyLocationEnabled(false);
    }

    @Override
    public void testGetBusResult(TestBus testBus) {
        MyLog.e(TAG, "回调");
        //获取测试校车路线点
        //校车路线数据
        List<LatLng> points = new ArrayList<>();
        for (int i = 0; i < testBus.getData().size(); i++) {
            LatLng p = new LatLng(Double.parseDouble(testBus.getData().get(i).getLatitude()), Double.parseDouble(testBus.getData().get(i).getLongitude()));
            points.add(p);
        }
        this.testBus = new PolylineOptions()
                .width(5)
                .color(Color.RED)
                .points(points);
    }

    @Override
    public void getMyStateResult(MyStateData myStateData) {
        tvUpBus.setVisibility(View.GONE);
        tvDownBus.setVisibility(View.GONE);
        switch (myStateData.getData().getState()) {
            case "0":
                tvState.setText("未预约");
                llBusInformation.setVisibility(View.GONE);
                break;
            case "1":
                tvState.setText("已预约");
                llBusInformation.setVisibility(View.VISIBLE);
                break;
            case "2":
                tvState.setText("校车已发车");
                llBusInformation.setVisibility(View.VISIBLE);
                if (myStateData.getData().getOnBus().equals("0")) {
                    tvUpBus.setVisibility(View.VISIBLE);
                    tvDownBus.setVisibility(View.GONE);
                } else {
                    tvUpBus.setVisibility(View.GONE);
                    tvDownBus.setVisibility(View.VISIBLE);
                }
                break;
        }
        if (myStateData.getData().getOnBus().equals("0")) {
            tvOnBus.setText("未上车");
        } else {
            tvOnBus.setText("已上车");
        }
        tvName.setText(myStateData.getData().getBus_name());
        tvBusId.setText(myStateData.getData().getBus_id());
        tvPlates.setText(myStateData.getData().getPlates());
        tvRunTime.setText(myStateData.getData().getRun_time());
    }

    @Override
    public void upBusResult(BaseData baseData) {
        showToast("上车成功");
        tvUpBus.setVisibility(View.GONE);
        tvDownBus.setVisibility(View.GONE);
        new Handler().postDelayed(this::getMyState, 1000);
    }

    @Override
    public void downBusResult(BaseData baseData) {
        showToast("下车成功");
        tvUpBus.setVisibility(View.GONE);
        tvDownBus.setVisibility(View.GONE);
        new Handler().postDelayed(this::getMyState, 1000);
    }

    @Override
    public void onError(Throwable e, String type) {
        switch (type) {
            case "getMyState":
                break;
            case "upBus":
            case "downBus":
                HttpUtil.onError(e);
                break;
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
