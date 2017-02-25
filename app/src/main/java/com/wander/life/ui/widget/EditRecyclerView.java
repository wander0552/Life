package com.wander.life.ui.widget;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.RecyclerView;
import android.util.AttributeSet;

/**
 * Created by wander on 2017/2/23.
 */

public class EditRecyclerView extends RecyclerView {
    public EditRecyclerView(Context context) {
        this(context,null);
    }

    public EditRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs,0);
    }

    public EditRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {


    }
}
