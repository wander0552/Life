package com.wander.life.ui.activity;

import android.os.Bundle;
import android.support.annotation.Nullable;

import com.wander.life.presenter.IPresenter;


/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
public abstract class PresenterActivity<T extends IPresenter> extends BaseLayerActivity {
    protected T mPresenter = null;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        mPresenter = getPresenter();

        initEventAndData();

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if (mPresenter != null) {
            mPresenter.onDetachView();
            mPresenter = null;
        }
    }

    protected abstract T getPresenter();

    protected abstract void initEventAndData();

}
