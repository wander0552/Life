package com.wander.life.ui.fragment;


import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wander.life.presenter.IPresenter;


/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
public abstract class PresenterFragment<T extends IPresenter> extends BaseFragment {
    protected T mPresenter;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        mContext = getActivity();
        mPresenter = getPresenter();
        super.onCreate(savedInstanceState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
    }

    protected abstract T getPresenter();
}
