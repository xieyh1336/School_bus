package com.example.school_bus.View;

import android.content.Context;
import android.util.AttributeSet;

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
