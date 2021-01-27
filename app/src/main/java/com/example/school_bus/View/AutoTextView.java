package com.example.school_bus.View;

import android.content.Context;
import android.util.AttributeSet;

/**
 * @作者 yonghe Xie
 * @创建/修改日期 2021-01-23 11:52
 * @类名 AutoTextView
 * @所在包 com\example\school_bus\View\AutoTextView.java
 * 自动滚动textView
 */
public class AutoTextView extends androidx.appcompat.widget.AppCompatTextView {
    public AutoTextView(Context context) {
        super(context);
    }

    public AutoTextView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public AutoTextView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    @Override
    public boolean isFocused() {
        return true;
    }
}
