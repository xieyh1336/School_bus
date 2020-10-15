package com.example.school_bus.View;

import android.content.Context;
import android.util.AttributeSet;
import android.widget.TextView;

public class AutoTextView extends TextView {
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
