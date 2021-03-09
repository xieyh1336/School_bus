package com.example.school_bus.Manage;

import com.baidu.mapapi.map.BaiduMap;
import com.baidu.mapapi.map.MarkerOptions;
import com.baidu.mapapi.map.OverlayOptions;
import com.baidu.mapapi.model.LatLng;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-03-08 14:23
 * @类名 MapDrawManage
 * @所在包 com\example\school_bus\Manage\MapDrawManage.java
 * 地图绘制管理类
 */
public class MapDrawManage {

    private List<OverlayOptions> options;
    private MarkerOptions click;
    private BaiduMap baiduMap;
    private boolean isShow = false;

    public MapDrawManage(BaiduMap baiduMap, MarkerOptions click){
        this.baiduMap = baiduMap;
        this.click = click;
        options = new ArrayList<>();
    }

    public void add(OverlayOptions overlayOptions){
        baiduMap.addOverlay(overlayOptions);
        options.add(overlayOptions);
    }

    public void updateClick(LatLng latLng){
        isShow = true;
        baiduMap.clear();
        click.position(latLng);
        for (int i = 0; i < options.size(); i++){
            baiduMap.addOverlay(options.get(i));
        }
        baiduMap.addOverlay(click);
    }

    public void showClick(){
        isShow = true;
        if (click.getPosition() != null){
            baiduMap.addOverlay(click);
        }
    }

    public void hideClick(){
        isShow = false;
        baiduMap.clear();
        for (int i = 0; i < options.size(); i++){
            baiduMap.addOverlay(options.get(i));
        }
    }

    public void clear(OverlayOptions overlayOptions){
        baiduMap.clear();//先将全部图层清除
        for (int i = 0; i < options.size(); i++){
            if (options.get(i) == overlayOptions){
                options.remove(i);
                break;
            }
        }
        for (int i = 0; i < options.size(); i++){
            baiduMap.addOverlay(options.get(i));
        }
        if (isShow && click.getPosition() != null){
            baiduMap.addOverlay(click);
        }
    }

    public void clearAll(){
        baiduMap.clear();
    }
}
