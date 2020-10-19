package com.example.school_bus.Adapter;

import android.content.Context;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.LayoutRes;
import androidx.annotation.Nullable;

import com.bumptech.glide.Glide;
import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.school_bus.Entity.NewsData;
import com.example.school_bus.R;
import com.example.school_bus.Utils.ResourcesUtil;

import java.util.List;

public class NewsRecyclerviewAdapter extends BaseQuickAdapter<NewsData.ResultBean, BaseViewHolder> {

    //布局文件
    private Context context;
    private OnItemClickListener listener;

    public NewsRecyclerviewAdapter(@LayoutRes int layoutResId, @Nullable List<NewsData.ResultBean> data, Context context){
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, NewsData.ResultBean item) {
        //获取当前item的类型
        helper.getItemViewType();
        final int position = helper.getLayoutPosition();
        TextView textView = helper.getView(R.id.textView);
        ImageView imageView = helper.getView(R.id.imageView);
        textView.setText(item.getTitle());
        Glide.with(context).load(item.getImage()).into(imageView);
        helper.setOnClickListener(R.id.item_more_news, view -> {
            if (listener != null){
                listener.OnItemClick(position);
            }
        });

        //测试保存图片
        imageView.setOnLongClickListener(view -> {
            Thread thread = new Thread(() ->
                    ResourcesUtil.SaveBitmapFromView(ResourcesUtil.getBitmap(item.getImage(), context), context));
            thread.start();
            return true;
        });

    }

    public interface OnItemClickListener{
        void OnItemClick(int position);
    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        this.listener = listener;
    }
}
