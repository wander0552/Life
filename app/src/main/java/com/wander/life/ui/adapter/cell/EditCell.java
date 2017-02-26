package com.wander.life.ui.adapter.cell;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;

import com.wander.life.R;
import com.wander.life.bean.LetterItem;
import com.wander.life.widget.recycler.RVBaseViewHolder;

/**
 * Created by wander on 2017/2/25.
 */

public class EditCell extends EditBaseCell<LetterItem> {
    public EditCell(LetterItem letterItem) {
        super(letterItem);
    }

    @Override
    public int getItemType() {
        return LetterItem.EDIT_TYPE;
    }

    @Override
    public EditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_layout,parent,false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {


    }

    class EditViewHolder extends RVBaseViewHolder{
        EditText mEditText;
        public EditViewHolder(View itemView) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_edit);
        }
    }


}
