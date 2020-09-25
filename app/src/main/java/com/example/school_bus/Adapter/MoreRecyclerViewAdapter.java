package com.example.school_bus.Adapter;

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
import com.example.school_bus.View.xRecyclerView;

public class MoreRecyclerViewAdapter extends xRecyclerView.xAdapter<MoreRecyclerViewAdapter.ViewHolder>{

    private NewsData newsData;
    private Context context;
    private OnItemClickListener listener;

    public MoreRecyclerViewAdapter(NewsData newsData) {
        this.newsData = newsData;
    }

    class ViewHolder extends RecyclerView.ViewHolder {
        private TextView textView;
        private ImageView imageView;

        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            textView = itemView.findViewById(R.id.textView);
            imageView = itemView.findViewById(R.id.imageView);
        }
    }

    public void setNewsData(NewsData newsData) {
        this.newsData = newsData;
    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }

    @Override
    protected int getxItemCount() {
        return newsData.getResult().size();
    }

    @Override
    protected ViewHolder onCreatexViewHolder(ViewGroup parent, int itemType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_news_recycle_view, null);
        context = parent.getContext();
        return new ViewHolder(view);
    }

    @Override
    protected void onBindxViewHolder(ViewHolder holder, int position) {
        holder.textView.setText(newsData.getResult().get(position).getTitle());
        Glide.with(context).load(newsData.getResult().get(position).getImage()).into(holder.imageView);
        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (listener != null){
                    listener.OnItemClick(position);
                }
            }
        });
    }

    @Override
    protected xRecyclerView.LoadMoreViewHolder createLoadMoreViewHolder(ViewGroup viewGroup) {
        return new xRecyclerView.LoadMoreViewHolder() {
            @Override
            public View createLoadMoreView() {
                return View.inflate(viewGroup.getContext(), R.layout.custome_load_more_item, null);
            }

            @Override
            public void changeLoadMoreViewState(boolean loading) {
                View bar = mLoadMoreView.findViewById(R.id.lm_pb);
                bar.setVisibility(loading ? View.VISIBLE : View.GONE);
            }
        };
    }
}
