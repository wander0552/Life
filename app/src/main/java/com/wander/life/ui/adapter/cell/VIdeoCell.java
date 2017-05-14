package com.wander.life.ui.adapter.cell;

import android.graphics.Bitmap;
import android.view.ViewGroup;

import com.wander.life.bean.LetterItem;
import com.wander.life.widget.recycler.RVBaseViewHolder;

/**
 * Created by wander on 2017/3/26.
 */

public class VIdeoCell extends EditBaseCell {
    public VIdeoCell(LetterItem letterItem) {
        super(letterItem);
    }

    @Override
    public int getItemType() {
        return 0;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    void editComplete() {

    }

    @Override
    public Bitmap screenshot() {
        return null;
    }
}
