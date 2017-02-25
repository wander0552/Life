package com.wander.life.widget.recycler.cell;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;

import com.wander.base.utils.DensityUtils;
import com.wander.life.R;
import com.wander.life.widget.recycler.RVBaseViewHolder;
import com.wander.life.widget.recycler.RVSimpleAdapter;


/**
 * Created by zhouwei on 17/1/23.
 */

public class LoadMoreCell extends RVAbsStateCell{
    public static final int mDefaultHeight = 80;//dp
    public LoadMoreCell(Object o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.LOAD_MORE_TYPE;
    }


    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {

    }

    @Override
    protected View getDefaultView(Context context) {
        // 设置LoadMore View显示的默认高度
        setHeight(DensityUtils.dp2px(context,mDefaultHeight));
        return LayoutInflater.from(context).inflate(R.layout.rv_load_more_layout,null);
    }
}
