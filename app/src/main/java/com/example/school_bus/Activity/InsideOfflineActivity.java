package com.example.school_bus.Activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.example.school_bus.R;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-03-04 11:58
 * @类名 InsideOfflineActivity
 * @所在包 com\example\school_bus\Activity\InsideOfflineActivity.java
 * 离线地图内页
 */
public class InsideOfflineActivity extends BaseActivity {

    private static MKOfflineMap mkOfflineMap;
    @BindView(R.id.tv_name)
    TextView tvName;
    @BindView(R.id.tv_size)
    TextView tvSize;
    @BindView(R.id.tv_delete)
    TextView tvDelete;
    @BindView(R.id.tv_introduce)
    TextView tvIntroduce;

    public static void getInstance(Context context, int cityID, String name, String size) {
        Intent intent = new Intent(context, InsideOfflineActivity.class);
        intent.putExtra("cityID", cityID);
        intent.putExtra("name", name);
        intent.putExtra("size", size);
        context.startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_inside_offline_map);
        ButterKnife.bind(this);
        init();
    }

    private void init() {
        Intent intent = getIntent();
        int cityID = intent.getIntExtra("cityID", 0);
        String name = intent.getStringExtra("name");
        String size = intent.getStringExtra("size");
        tvName.setText(name);
        tvSize.setText(size);
        tvIntroduce.setText("下载离线地图数据包，没有网络也能浏览地图。体验更快更稳定，下载后无需安装即可使用。");

        tvDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent1 = new Intent("offline");
                intent1.putExtra("type", 1);
                intent1.putExtra("cityID", cityID);
                sendBroadcast(intent1);
                finish();
            }
        });
    }
}