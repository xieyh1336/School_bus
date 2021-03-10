package com.example.school_bus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Entity.BusListData;
import com.example.school_bus.R;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-03-10 15:23
 * @类名 WaitingBusListAdapter
 * @所在包 com\example\school_bus\Adapter\WaitingBusListAdapter.java
 * 等待发车列表适配器
 */
public class WaitingBusListAdapter extends RecyclerView.Adapter<WaitingBusListAdapter.ViewHolder> {

    private BusListData data;
    private OnClickListener onClickListener;

    public void setData(BusListData data) {
        this.data = data;
        notifyDataSetChanged();
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void onClick(int position, BusListData data);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_waiting_bus, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvName.setText(data.getData().get(position).getName());
        holder.tvPlates.setText(data.getData().get(position).getPlates());
        holder.tvNumOfOrder.setText(data.getData().get(position).getNum_of_order());
        holder.tvRunTime.setText(data.getData().get(position).getRun_time());
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null){
                onClickListener.onClick(position, data);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (data != null && data.getData() != null){
            return data.getData().size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvName, tvPlates, tvNumOfOrder, tvRunTime;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvName = itemView.findViewById(R.id.tv_name);
            tvPlates = itemView.findViewById(R.id.tv_plates);
            tvNumOfOrder = itemView.findViewById(R.id.tv_num_of_order);
            tvRunTime = itemView.findViewById(R.id.tv_run_time);
        }
    }
}
