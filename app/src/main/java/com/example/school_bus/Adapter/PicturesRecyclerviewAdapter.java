package com.example.school_bus.Adapter;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.drawable.Drawable;
import android.os.Looper;
import android.view.View;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.Nullable;

import com.chad.library.adapter.base.BaseQuickAdapter;
import com.chad.library.adapter.base.BaseViewHolder;
import com.example.school_bus.R;
import com.example.school_bus.Utils.FileUtil;
import com.github.ybq.android.spinkit.SpinKitView;
import com.github.ybq.android.spinkit.SpriteFactory;
import com.github.ybq.android.spinkit.Style;
import com.github.ybq.android.spinkit.sprite.Sprite;

import java.util.List;

public class PicturesRecyclerviewAdapter extends BaseQuickAdapter<Drawable, BaseViewHolder> {

    private Context context;
    private OnItemClickListener listener;

    public PicturesRecyclerviewAdapter(int layoutResId, @Nullable List<Drawable> data, Context context) {
        super(layoutResId, data);
        this.context = context;
    }

    @Override
    protected void convert(BaseViewHolder helper, Drawable item) {
        //获取当前item的类型
        helper.getItemViewType();
        final int position = helper.getLayoutPosition();
        ImageView imageView = helper.getView(R.id.imageView);
        SpinKitView spinKitView = helper.getView(R.id.skv);

        Style style = Style.values()[position % 15];
        Sprite drawable = SpriteFactory.create(style);
        spinKitView.setIndeterminateDrawable(drawable);

        Bitmap bitmap = FileUtil.drawableToBitmap(item);
        imageView.setImageBitmap(bitmap);
//        imageView.setImageDrawable(item);
        spinKitView.setVisibility(View.GONE);
//        Glide
//                .with(context)
//                .load("https://api.ixiaowai.cn/api/api.php")
//                .diskCacheStrategy(DiskCacheStrategy.NONE)//关闭磁盘缓存
//                .skipMemoryCache(true)//跳过内存缓存
//                .into(new SimpleTarget<GlideDrawable>() {
//            @Override
//            public void onResourceReady(GlideDrawable resource, GlideAnimation<? super GlideDrawable> glideAnimation) {
//                //加载完成后的处理
//                imageView.setImageDrawable(resource);
//                spinKitView.setVisibility(View.GONE);
//            }
//        });
        imageView.setOnClickListener(view -> {
            if (listener != null){
                listener.OnItemClick(position);
            }
        });
        //测试保存图片
        imageView.setOnLongClickListener(view -> {
            Thread thread = new Thread(() -> {
                FileUtil.SaveBitmapFromView(bitmap, context);
                Looper.prepare();
                Toast.makeText(context, "保存成功", Toast.LENGTH_SHORT).show();
                Looper.loop();
//                    FileUtil.SaveBitmapFromView(FileUtil.getBitmap(item.getImg(), context), context);
            });
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
