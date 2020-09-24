package com.example.school_bus.View;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PaintFlagsDrawFilter;
import android.util.AttributeSet;
import android.widget.RelativeLayout;

import com.example.school_bus.R;

/**
 *点击菜单栏弹出的侧边栏的波浪背景
 * 类似支付宝的蓝色背景波纹
 */
public class WaveView extends RelativeLayout {

    private PaintFlagsDrawFilter drawFilter;
    private Paint paint;
    //三角函数的初相
    private float mOffset1 = 0.0f;
    private float mOffset2 = 0.0f;
    private float mSpeed1 = 0.05f;
    private float mSpeed2 = 0.07f;
    public WaveView(Context context) {
        super(context);
        initView();
    }

    public WaveView(Context context, AttributeSet attrs) {
        super(context, attrs);
        initView();
    }

    public WaveView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView();
    }

    @SuppressLint("NewApi")
    private void initView(){
        //初始绘制波纹的画笔
        paint = new Paint();
        //抗锯齿
        paint.setAntiAlias(true);
        //实线风格
        paint.setStyle(Paint.Style.FILL);
        //设置画笔颜色
        paint.setColor(getResources().getColor(R.color.colorPrimaryDark,null));
        drawFilter = new PaintFlagsDrawFilter(0, Paint.ANTI_ALIAS_FLAG | Paint.FILTER_BITMAP_FLAG);
    }

    @Override
    protected void dispatchDraw(Canvas canvas) {
        initDrawBg(canvas);//放在super前面是后景，相反是前景，前景会覆盖子布局
        super.dispatchDraw(canvas);
    }

    private void initDrawBg(Canvas canvas){
        //从canvas层面去除绘制的锯齿
        canvas.setDrawFilter(drawFilter);
        for (int i = 0; i < getWidth(); i++){
            //y = A *sin( wx + b) + h ; A：浪高； w：周期； b：初相；h： y轴的偏移位置
            float endY = (float) (20 * Math.sin(2 * Math.PI / getWidth() * i + mOffset1) + 330);
            //h：是y轴方向的偏移量
            //startY：0就是从最顶部开始
            //画第一条波浪
            canvas.drawLine(i, 0, i, endY, paint);

            float endY2 = (float) (20 * Math.sin(2 * Math.PI / getWidth() * i + mOffset2) + 330);
            //画第二条波浪
            canvas.drawLine(i, 0, i, endY2, paint);
        }

        //防止数值超过浮点型的最大值
        if (mOffset1 > Float.MAX_VALUE - 1){
            mOffset1 = 0;
        }
        mOffset1 += mSpeed1;
        if (mOffset2 > Float.MAX_VALUE - 1){
            mOffset2 = 0;
        }
        mOffset2 += mSpeed2;
        postInvalidate();
    }
}
