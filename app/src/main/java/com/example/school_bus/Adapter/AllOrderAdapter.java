package com.example.school_bus.Adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.Entity.AllOrderData;
import com.example.school_bus.R;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-03-17 16:10
 * @类名 AllOrderAdapter
 * @所在包 com\example\school_bus\Adapter\AllOrderAdapter.java
 * 所有订单适配器
 */
public class AllOrderAdapter extends RecyclerView.Adapter<AllOrderAdapter.ViewHolder>{

    private AllOrderData allOrderData;

    public void setData(AllOrderData allOrderData) {
        this.allOrderData = allOrderData;
        notifyDataSetChanged();
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_all_order, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvId.setText(allOrderData.getData().get(position).getBus_id());
        switch (allOrderData.getData().get(position).getType()){
            case "0":
                holder.tvState.setText("未完成");
                break;
            case "1":
                holder.tvState.setText("已完成");
                break;
            case "2":
                holder.tvState.setText("已取消");
                break;
            default:
                holder.tvState.setText("");
        }
        holder.tvPlates.setText(allOrderData.getData().get(position).getPlates());
        holder.tvTime.setText(allOrderData.getData().get(position).getCreate_time());
    }

    @Override
    public int getItemCount() {
        if (allOrderData != null && allOrderData.getData() != null){
            return allOrderData.getData().size();
        }
        return 0;
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvId, tvTime, tvPlates, tvState;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvId = itemView.findViewById(R.id.tv_id);
            tvTime = itemView.findViewById(R.id.tv_time);
            tvPlates = itemView.findViewById(R.id.tv_plates);
            tvState = itemView.findViewById(R.id.tv_state);
        }
    }
}
