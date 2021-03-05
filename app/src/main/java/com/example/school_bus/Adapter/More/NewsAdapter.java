package com.example.school_bus.Adapter.More;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.R;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-03 14:55
 * @类名 NewsAdapter
 * @所在包 com\example\school_bus\Adapter\NewsAdapter.java
 * 更多，新闻适配器
 */
public class NewsAdapter extends RecyclerView.Adapter<NewsAdapter.ViewHolder> {

    private Context context;
    private List<NewsData.ResultBean> list;
    private OnClickListener onClickListener;

    public NewsAdapter(Context context) {
        this.context = context;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(int position);
    }

    public void setData(List<NewsData.ResultBean> list) {
        this.list = list;
        notifyDataSetChanged();
    }

    public void addData(List<NewsData.ResultBean> list){
        if (this.list == null){
            this.list = new ArrayList<>();
        }
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<NewsData.ResultBean> getData() {
        return list;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_news_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        holder.tvNews.setText(list.get(position).getTitle());
        Glide.with(context).load(list.get(position).getImage()).into(holder.ivNews);
        holder.itemView.setOnClickListener(v -> {
            if (onClickListener != null){
                onClickListener.OnClick(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        if (list != null){
            return list.size();
        } else {
            return 0;
        }
    }

    static class ViewHolder extends RecyclerView.ViewHolder {

        TextView tvNews;
        ImageView ivNews;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            tvNews = itemView.findViewById(R.id.tv_news);
            ivNews = itemView.findViewById(R.id.iv_news);
        }
    }
}
