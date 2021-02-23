package com.example.school_bus.View;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import androidx.annotation.Nullable;

import com.example.school_bus.R;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-02-05 16:30
 * @类名 DownloadView
 * @所在包 com\example\school_bus\View\DownloadView.java
 * 下载进度圈
 */
public class DownloadView extends View {

    private int width, height, circleWidth;
    private int color;
    private Paint circlePaint = new Paint();
    private Paint centerPaint = new Paint();
    private float max = 100, current = 0;//最大值默认100
    private RectF rectF;
    private Path path = new Path();
    public final static int START = 1;
    public final static int PAUSE = 2;
    private int state;//当前状态

    /**
     * 该方法在java代码中new会用到
     * @param context context
     */
    public DownloadView(Context context) {
        super(context);
    }

    /**
     * 该方法在xml布局文件中使用时调用
     * @param context context
     * @param attrs attrs
     */
    public DownloadView(Context context, @Nullable AttributeSet attrs) {
        super(context, attrs);
        init(context, attrs);
    }

    /**
     * 该方法不会自动调用，如果有默认的style时，会在第二个构造函数调用
     * @param context context
     * @param attrs attrs
     * @param defStyleAttr defStyleAttr
     */
    public DownloadView(Context context, @Nullable AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        init(context, attrs);
    }

    private void init(Context context, @Nullable AttributeSet attr){
        //获取xml配置值
        TypedArray typedArray = context.obtainStyledAttributes(attr, R.styleable.DownloadView);
        color = typedArray.getColor(R.styleable.DownloadView_color, context.getResources().getColor(R.color.theme));
        typedArray.recycle();

        circlePaint.setAntiAlias(true);//消除锯齿
        centerPaint.setAntiAlias(true);//消除锯齿
        circlePaint.setColor(color);
        centerPaint.setColor(color);
        circlePaint.setStyle(Paint.Style.STROKE);//绘制空心圆
        state = START;//默认初始状态是开始
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        width = MeasureSpec.getSize(widthMeasureSpec);
        height = MeasureSpec.getSize(heightMeasureSpec);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onDraw(Canvas canvas) {
        super.onDraw(canvas);
        circleWidth = width / 6;
        circlePaint.setStrokeWidth(circleWidth);
        if (rectF == null){
            rectF = new RectF((float) circleWidth / 2, (float) circleWidth / 2, width - (float) circleWidth / 2, height - (float) circleWidth / 2);
        }
        canvas.drawArc(rectF, 270, 360 * current / max, false, circlePaint);
        if (state == START){
            //开始状态，显示暂停的按钮
            canvas.drawRect((float) width / 3, (float) height * 2 / 7, (float) width * 5 / 12, (float) height * 5 / 7, centerPaint);
            canvas.drawRect((float) width * 7 / 12, (float) height * 2 / 7, (float) width * 2 / 3, (float) height * 5 / 7, centerPaint);
        } else {
            path.moveTo((float) width * 4 / 10, (float) height * 2 / 7);
            path.lineTo((float) width * 4 / 10, (float) height * 5 / 7);
            path.lineTo((float) width * 7 / 10, (float) height / 2);
            path.close();
            canvas.drawPath(path, centerPaint);
        }
    }

    /**
     * 设置最大值
     * @param max 最大值
     */
    public void setMax(float max) {
        this.max = max;
        invalidate();
    }

    /**
     * 设置当前值
     * @param current 当前值
     */
    public void setCurrent(float current) {
        this.current = current;
        invalidate();
    }

    /**
     * 设置当前状态
     * @param state 当前状态
     */
    public void setState(int state) {
        this.state = state;
        invalidate();
    }

    /**
     * 获取当前状态
     * @return 当前状态
     */
    public int getState(){
        return state;
    }
}
