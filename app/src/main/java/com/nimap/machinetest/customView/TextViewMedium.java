package com.nimap.machinetest.customView;

import android.content.Context;
import android.graphics.Typeface;
import android.util.AttributeSet;
import android.widget.TextView;

public class TextViewMedium extends TextView {

    public TextViewMedium(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    public TextViewMedium(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();
    }

    public TextViewMedium(Context context) {
        super(context);
        init();
    }

    public void init() {
        Typeface tf = Typeface.createFromAsset(getContext().getAssets(), "roboto-medium.ttf");
        setTypeface(tf);
    }
}
