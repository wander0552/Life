package com.wander.life.ui.adapter.cell;

import android.graphics.Bitmap;
import android.text.Editable;
import android.text.Spannable;
import android.text.SpannableStringBuilder;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.TextView;

import com.wander.base.log.WLog;
import com.wander.life.R;
import com.wander.life.bean.LetterItem;
import com.wander.life.utils.ToastUtils;
import com.wander.life.widget.recycler.RVBaseViewHolder;

/**
 * Created by wander on 2017/2/25.
 */

public class EditCell extends EditBaseCell {
    public EditCell(LetterItem letterItem) {
        super(letterItem);
    }

    @Override
    void editComplete() {

    }

    @Override
    public Bitmap screenshot() {
        Bitmap bitmap = null;
        if (mHolder != null) {
            mHolder.itemView.setDrawingCacheEnabled(true);
            mHolder.itemView.buildDrawingCache();
            bitmap = mHolder.itemView.getDrawingCache();
        }
        return bitmap;
    }


    @Override
    public int getItemType() {
        return LetterItem.EDIT_TYPE;
    }

    @Override
    public EditViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new EditViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_edit_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof EditViewHolder) {
        EditViewHolder mHolder = (EditViewHolder) holder;
            String detail = mData.getDetail();
            if (TextUtils.isEmpty(detail)){
                mHolder.mEditText.setHint("请输入内容");
            }else {
                mHolder.mEditText.setText(detail);
                mHolder.mEditText.append("\n");
            }
            Spannable spannable = new SpannableStringBuilder();
            mHolder.mEditText.setOnFocusChangeListener(new View.OnFocusChangeListener() {
                @Override
                public void onFocusChange(View v, boolean hasFocus) {

                    if (hasFocus){
                        mHolder.mEditText.moveCursorToVisibleOffset();
                    }
                }
            });
            ((EditViewHolder) holder).mEditText.setOnKeyListener(new View.OnKeyListener() {
                @Override
                public boolean onKey(View v, int keyCode, KeyEvent event) {
                    WLog.e("keycode", "\t" + keyCode);
                    switch (keyCode) {
                        case EditorInfo.IME_ACTION_DONE:
                            ToastUtils.makeTextShort("done");
                            break;
                        case EditorInfo.IME_ACTION_NEXT:
                            ToastUtils.makeTextShort("next");
                            break;
                        case KeyEvent.KEYCODE_DEL:
                            ToastUtils.makeTextShort("key del");
                            break;
                        case KeyEvent.KEYCODE_SPACE:
                            ToastUtils.makeTextShort("space");
                            break;
                    }
                    return false;
                }
            });

            ((EditViewHolder) holder).mEditText.addTextChangedListener(new TextWatcher() {
                @Override
                public void beforeTextChanged(CharSequence s, int start, int count, int after) {

                }

                @Override
                public void onTextChanged(CharSequence s, int start, int before, int count) {

                }

                @Override
                public void afterTextChanged(Editable s) {
                    mData.setDetail(s.toString());

                }
            });
        } else {

        }


    }

    @Override
    public void releaseResource() {
        super.releaseResource();
    }

    class EditViewHolder extends RVBaseViewHolder {
        EditText mEditText;

        public EditViewHolder(View itemView) {
            super(itemView);
            mEditText = (EditText) itemView.findViewById(R.id.item_edit);
        }
    }


}
