package com.wander.life.presenter;


import com.wander.life.ui.iviews.IView;

/**
 * Created by wander on 2016/6/15.
 * email 805677461@qq.com
 */
public interface IPresenter<T extends IView> {
    void onAttachView(T mView);

    void onDetachView();

}
