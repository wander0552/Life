package com.wander.life.widget.recycler.cell;

import android.content.Context;
import android.text.TextUtils;
import android.view.LayoutInflater;
import android.view.View;

import com.wander.life.R;
import com.wander.life.widget.recycler.RVBaseViewHolder;
import com.wander.life.widget.recycler.RVSimpleAdapter;


/**
 * Created by zhouwei on 17/1/23.
 */

public class ErrorCell extends RVAbsStateCell<String> {
    public ErrorCell(String o) {
        super(o);
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.ERROR_TYPE;
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        if (!TextUtils.isEmpty(mData)) {
            holder.setText(R.id.cell_error, mData);
        }
    }

    @Override
    protected View getDefaultView(Context context) {
        return LayoutInflater.from(context).inflate(R.layout.rv_error_layout, null);
    }
}
