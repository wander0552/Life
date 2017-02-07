package com.wander.life.presenter;

import android.content.Context;

import com.wander.life.ui.iviews.IWriteView;

/**
 * Created by wander on 2017/2/6.
 */

public class WritePresenter extends BasePresenter<IWriteView> {
    public WritePresenter(Context context, IWriteView mView) {
        super(context, mView);
    }
}
