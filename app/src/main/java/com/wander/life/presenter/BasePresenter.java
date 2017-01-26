package com.wander.life.presenter;

import android.content.Context;

import com.wander.life.ui.iviews.IView;

import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;


/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
public abstract class BasePresenter<T extends IView> implements IPresenter<T> {
    protected Context mContext;
    protected T mView;
    /**
     * 用于回收异步处理的内存
     */
    protected CompositeDisposable mCompositeDisposable;

    public BasePresenter(Context context, T mView) {
        this.mContext = context;
        onAttachView(mView);
    }

    protected void unSubscribe() {

        if (mCompositeDisposable != null && !mCompositeDisposable.isDisposed()) {
            mCompositeDisposable.clear();
        }
    }

    protected void addSubscribe(Disposable subscription) {
        if (mCompositeDisposable == null) {
            mCompositeDisposable = new CompositeDisposable();
        }
        mCompositeDisposable.add(subscription);
    }

    public boolean isViewAttach() {
        return mView != null;
    }

    @Override
    public void onAttachView(T mView) {
        this.mView = mView;
    }

    @Override
    public void onDetachView() {
        unSubscribe();
    }
}
