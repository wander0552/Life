package com.wander.life.ui.adapter.cell;

import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;

import com.wander.life.bean.LetterItem;
import com.wander.life.widget.recycler.RVBaseCell;
import com.wander.life.widget.recycler.RVBaseViewHolder;

/**
 * Created by wander on 2017/2/25.
 */

public abstract class EditBaseCell extends RVBaseCell<LetterItem> {
    public RVBaseViewHolder mHolder;

    public EditBaseCell(LetterItem letterItem) {
        super(letterItem);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        mHolder = holder;
    }

    abstract void editComplete();

    public abstract Bitmap screenshot();

    public int getHeight(RecyclerView recyclerView) {
        if (mHolder == null) {
            return 0;
        }
        onBindViewHolder(mHolder, 0);
//        mHolder.itemView.measure(View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.AT_MOST),
//                View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED));
//        mHolder.itemView.layout(0, 0, mHolder.itemView.getMeasuredWidth(), mHolder.itemView.getMeasuredHeight());


        int measuredHeight = mHolder.getItemView().getMeasuredHeight();
        return measuredHeight;
    }
}
