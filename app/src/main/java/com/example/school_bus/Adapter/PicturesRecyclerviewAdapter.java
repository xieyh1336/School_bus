package com.example.school_bus.Adapter;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.school_bus.R;
import com.example.school_bus.Utils.FileUtil;
import com.example.school_bus.Utils.MyLog;

import java.util.ArrayList;
import java.util.List;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-03 14:17
 * @类名 PicturesRecyclerviewAdapter
 * @所在包 com\example\school_bus\Adapter\PicturesRecyclerviewAdapter.java
 * 更多，美图适配器
 */
public class PicturesRecyclerviewAdapter extends RecyclerView.Adapter<PicturesRecyclerviewAdapter.ViewHolder> {

    private Context context;
    private List<Drawable> list = new ArrayList<>();
    private OnClickListener onClickListener;

    public PicturesRecyclerviewAdapter(Context context) {
        this.context = context;
    }

    public void setData(List<Drawable> list) {
        this.list.clear();
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public void addData(List<Drawable> list){
        this.list.addAll(list);
        notifyDataSetChanged();
    }

    public List<Drawable> getData() {
        return list;
    }

    public void setOnClickListener(OnClickListener onClickListener) {
        this.onClickListener = onClickListener;
    }

    public interface OnClickListener{
        void OnClick(int position);
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_more_pictures_recyclerview, parent, false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolder holder, int position) {
        Bitmap bitmap = FileUtil.drawableToBitmap(list.get(position));
        holder.image.setImageBitmap(bitmap);//drawable会造成卡顿，改用bitmap
        holder.image.setOnClickListener(view -> {
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
        ImageView image;
        public ViewHolder(@NonNull View itemView) {
            super(itemView);
            image = itemView.findViewById(R.id.image);
        }
    }
}
