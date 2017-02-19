package com.wander.life.presenter;

import android.content.Context;

import com.wander.life.bean.Letter;
import com.wander.life.database.DbUtils;
import com.wander.life.ui.iviews.IWriteView;

/**
 * Created by wander on 2017/2/6.
 */

public class WritePresenter extends BasePresenter<IWriteView> {
    private Letter mLetter;
    public WritePresenter(Context context, IWriteView mView) {
        super(context, mView);
        mLetter = new Letter();
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
        String content = mView.getContent();
        mLetter.setContent(content);
        DbUtils.saveLetter(mLetter);
    }
}
