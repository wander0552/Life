package com.wander.life.presenter;

import android.content.Context;

import com.wander.life.bean.Letter;
import com.wander.life.ui.iviews.IWriteView;

/**
 * Created by wander on 2017/2/6.
 */

public class WritePresenter extends BasePresenter<IWriteView> {
    private Letter mLetter;
    public WritePresenter(Context context, IWriteView mView) {
        super(context, mView);
    }

    public Letter getmLetter() {
        return mLetter;
    }

    public void setmLetter(Letter mLetter) {
        this.mLetter = mLetter;
    }
}
