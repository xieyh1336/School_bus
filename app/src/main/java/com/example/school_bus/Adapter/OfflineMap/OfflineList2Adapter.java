package com.example.school_bus.Adapter.OfflineMap;

import android.annotation.SuppressLint;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.baidu.mapapi.map.offline.MKOLSearchRecord;
import com.baidu.mapapi.map.offline.MKOLUpdateElement;
import com.baidu.mapapi.map.offline.MKOfflineMap;
import com.example.school_bus.Activity.InsideOfflineActivity;
import com.example.school_bus.R;
import com.example.school_bus.Utils.FileUtil;
import com.example.school_bus.Utils.MyLog;
import com.example.school_bus.Utils.ScreenUtils;
import com.example.school_bus.View.DownloadView;

import java.util.List;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-23 15:54
 * @类名 OfflineList2Adapter
 * @所在包 com\example\school_bus\Adapter\OfflineMap\OfflineList2Adapter.java
 * 离线地图，全部城市2级适配器
 */
public class OfflineList2Adapter extends RecyclerView.Adapter<OfflineList2Adapter.ViewHolder> {

    private static String TAG = "OfflineList2Adapter";
    private Context context;
    private MKOfflineMap mkOfflineMap;
    private List<MKOLSearchRecord> allCity;//所有支持离线的城市

    public OfflineList2Adapter(Context context) {
        this.context = context;
    }

    public void setData(MKOfflineMap mkOfflineMap, List<MKOLSearchRecord> allCity){
        this.mkOfflineMap = mkOfflineMap;
        this.allCity = allCity;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_offline_map_list1, parent, false);
        return new ViewHolder(view);
    }

    @SuppressLint("SetTextI18n")
    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        //设置间距
        ((LinearLayout.LayoutParams) holder.tvName.getLayoutParams())
                .setMargins(ScreenUtils.dip2px(context, 10), 0, 0, 0);

        holder.tvName.setText(allCity.get(position).cityName);//城市名
        holder.tvSize.setText(FileUtil.getFormatSize(allCity.get(position).dataSize));//包大小
        holder.tvState.setVisibility(View.GONE);//隐藏“已下载”
        holder.vDownload.setVisibility(View.GONE);//隐藏下载圈
        holder.tvFunction.setVisibility(View.VISIBLE);//显示下载按钮
        holder.tvFunction.setText("下载");
        holder.rvList.setVisibility(View.GONE);//隐藏二级列表
        holder.ivRight.setImageResource(R.drawable.ic_arrow_right);//右方向箭头
        holder.setIsRecyclable(false);//不复用
        //查找已下载的城市列表，如果一个都没有下载的话则列表为空对象
        if (mkOfflineMap.getAllUpdateInfo() != null && mkOfflineMap.getAllUpdateInfo().size() != 0) {
            for (int i = 0; i < mkOfflineMap.getAllUpdateInfo().size(); i++) {
                if (mkOfflineMap.getAllUpdateInfo().get(i).cityID == allCity.get(position).cityID) {
                    //离线地图存在时
                    MyLog.e(TAG, "status：" + mkOfflineMap.getAllUpdateInfo().get(i).status);
                    switch (mkOfflineMap.getAllUpdateInfo().get(i).status) {
                        case MKOLUpdateElement.FINISHED:
                            //已下载完成的
                            MyLog.e(TAG, "下载完成");
                            holder.tvState.setVisibility(View.VISIBLE);
                            holder.tvState.setText("已下载");
                            holder.tvFunction.setVisibility(View.GONE);
                            break;
                        case MKOLUpdateElement.WAITING:
                            //等待下载的
                            MyLog.e(TAG, "等待下载");
                            holder.tvState.setVisibility(View.VISIBLE);
                            holder.tvState.setText("正在等待");
                            holder.tvFunction.setVisibility(View.GONE);
                            holder.vDownload.setVisibility(View.VISIBLE);
                            holder.vDownload.setMax(allCity.get(position).dataSize);//设置最大值
                            holder.vDownload.setCurrent(mkOfflineMap.getAllUpdateInfo().get(i).size);//当前已下载的数据大小
                            holder.vDownload.setState(DownloadView.START);//开始状态的
                            break;
                        case MKOLUpdateElement.SUSPENDED:
                            //已暂停的
                            MyLog.e(TAG, "已暂停");
                            holder.tvState.setVisibility(View.VISIBLE);
                            holder.tvState.setText("已暂停");
                            holder.tvFunction.setVisibility(View.GONE);
                            holder.vDownload.setVisibility(View.VISIBLE);
                            holder.vDownload.setMax(allCity.get(position).dataSize);//设置最大值
                            holder.vDownload.setCurrent(mkOfflineMap.getAllUpdateInfo().get(i).size);//当前已下载的数据大小
                            holder.vDownload.setState(DownloadView.PAUSE);//暂停状态的
                            break;
                        case MKOLUpdateElement.DOWNLOADING:
                            //正在下载的
                            MyLog.e(TAG, "正在下载");
                            holder.tvState.setVisibility(View.VISIBLE);
                            holder.tvState.setText(FileUtil.getFormatSize(mkOfflineMap.getAllUpdateInfo().get(i).size) + "/" + FileUtil.getFormatSize(allCity.get(position).dataSize));
                            holder.tvFunction.setVisibility(View.GONE);
                            holder.vDownload.setVisibility(View.VISIBLE);
                            holder.vDownload.setMax(allCity.get(position).dataSize);//设置最大值
                            holder.vDownload.setCurrent(mkOfflineMap.getAllUpdateInfo().get(i).size);//当前已下载的数据大小
                            holder.vDownload.setState(DownloadView.START);//开始状态的
                            break;
                        case MKOLUpdateElement.eOLDSInstalling:
                            //离线包导入中
                            MyLog.e(TAG, "离线包导入中");
                            holder.tvState.setVisibility(View.VISIBLE);
                            holder.tvState.setText("导入中...");
                            holder.tvFunction.setVisibility(View.GONE);
                            holder.itemView.post(this::notifyDataSetChanged);
                            break;
                    }
                }
            }
        }
        //功能键
        holder.tvFunction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.e(TAG, "点击了" + holder.tvFunction.getText().toString());
                if (holder.tvFunction.getText().toString().equals("下载")){
                    mkOfflineMap.start(allCity.get(position).cityID);//开始下载
                    notifyDataSetChanged();
                }
            }
        });
        holder.vDownload.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                MyLog.e(TAG, "点击了下载圈");
                if (holder.vDownload.getState() == DownloadView.START){
                    //开始状态的，则暂停
                    if (mkOfflineMap.pause(allCity.get(position).cityID)){
                        //暂停成功
                        notifyDataSetChanged();
                    }
                } else {
                    //暂停状态的，则继续
                    if (mkOfflineMap.start(allCity.get(position).cityID)){
                        //开始成功
                        notifyDataSetChanged();
                    }
                }
            }
        });
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                InsideOfflineActivity.getInstance(context, allCity.get(position).cityID, allCity.get(position).cityName, FileUtil.getFormatSize(allCity.get(position).dataSize));
            }
        });
    }

    @Override
    public int getItemCount() {
        if (allCity != null){
            return allCity.size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvSize, tvState, tvFunction;
        RecyclerView rvList;
        DownloadView vDownload;
        ImageView ivRight;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvSize = itemView.findViewById(R.id.tv_size);
            tvState = itemView.findViewById(R.id.tv_state);
            tvFunction = itemView.findViewById(R.id.tv_function);
            rvList = itemView.findViewById(R.id.rv_list);
            vDownload = itemView.findViewById(R.id.v_download);
            ivRight = itemView.findViewById(R.id.iv_right);
        }
    }
}
