package com.wander.life.widget.recycler.cell;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.wander.base.utils.DensityUtils;
import com.wander.life.R;
import com.wander.life.widget.recycler.RVBaseCell;
import com.wander.life.widget.recycler.RVBaseViewHolder;
import com.wander.life.widget.recycler.RVSimpleAdapter;


/**
 * Created by wander on 2017/4/1.
 */

public class BidLineCell extends RVBaseCell<Integer> {
    private Context context;
    private int color = Color.BLACK;
    private int height = 1;

    public BidLineCell(Context context) {
        this.context = context;
    }

    public BidLineCell(Context context, int color, int height) {
        this.context = context;
        this.color = color;
        this.height = height;
    }


    @Override
    public int getItemType() {
        return RVSimpleAdapter.LINE_TYPE;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        Context context = parent.getContext();
        View view = LayoutInflater.from(context).inflate(R.layout.cell_line, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        View view = holder.getView(R.id.cell_line);
        view.setPadding(0, 0, 0, DensityUtils.dp2px(context, height));
        view.setBackgroundColor(color);
    }

//    public static BidLineCell createBig(Context mContext) {
//    }
}
