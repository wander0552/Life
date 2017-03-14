package com.wander.life.presenter;

import android.content.Context;

import com.wander.life.bean.Letter;
import com.wander.life.bean.LetterItem;
import com.wander.life.database.DbUtils;
import com.wander.life.ui.iviews.IEditView;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wander on 2017/2/26.
 */

public class EditPresenter extends BasePresenter<IEditView> {
    private Letter mLetter;
    private List<LetterItem> mList;

    public EditPresenter(Context context, IEditView mView) {
        super(context, mView);
        mLetter = new Letter();
        mList = new ArrayList<>();
    }

    public Letter getLetter() {
        return mLetter;
    }

    public void setLetter(Letter mLetter) {
        this.mLetter = mLetter;
    }

    public void saveLetter() {
        mLetter.setType(Letter.LETTER_TYPE_DRAFT);
        mLetter.setAddress("test");
        mLetter.setContent("hai mei zhunbeihao shuju");
        DbUtils.updateOrInstert(mLetter);
    }

    public LetterItem createEditItem() {
        LetterItem letterItem = new LetterItem();
        letterItem.setDetail("presenter create");
        mList.add(letterItem);
        return letterItem;

    }
}
