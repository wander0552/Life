package com.wander.life.widget.recycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wander.life.R;
import com.wander.life.widget.recycler.RVBaseViewHolder;
import com.wander.life.widget.recycler.RVSimpleAdapter;


/**
 * Created by zhouwei on 17/1/23.
 */

public class EmptyCell extends RVAbsStateCell {

    public EmptyCell(Object o) {
        super(o);
    }


    @Override
    public int getItemType() {
        return RVSimpleAdapter.EMPTY_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

    }

    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_empty_layout,null);
    }
}
