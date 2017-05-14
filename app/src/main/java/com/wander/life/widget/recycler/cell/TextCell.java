package com.wander.life.widget.recycler.cell;

import android.graphics.Color;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.wander.life.R;
import com.wander.life.widget.recycler.RVBaseCell;
import com.wander.life.widget.recycler.RVBaseViewHolder;
import com.wander.life.widget.recycler.RVSimpleAdapter;


/**
 * Created by wander on 2017/4/1.
 */

public class TextCell extends RVBaseCell<String> {
    private int color = Color.BLACK;
    private int size = 15;

    public TextCell(String s) {
        super(s);
    }

    public TextCell(String s, int color) {
        super(s);
        this.color = color;
    }

    public TextCell(String s, int color, int size) {
        super(s);
        this.color = color;
        this.size = size;
    }

    @Override
    public int getItemType() {
        return RVSimpleAdapter.TEXT_TYPE;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.cell_text, parent, false);
        return new RVBaseViewHolder(view);
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        TextView textView = holder.getTextView(R.id.cell_empty);
        textView.setText( mData == null ? "暂无记录" : Html.fromHtml(mData));
        textView.setTextSize(size);
        textView.setTextColor(color);
    }
}
