package com.wander.life.widget.recycler;

import android.view.ViewGroup;

/**
 * Created by zhouwei on 17/1/19.
 */

public  abstract class RVBaseCell<T> implements Cell {
    protected String TAG = "RVBaseCell";


    public RVBaseCell() {
    }

    public RVBaseCell(T t){
        mData = t;
    }
    public T mData;

    public void setData(T mData) {
        this.mData = mData;
    }

    public T getData() {
        return mData;
    }

    @Override
    public void releaseResource() {
        // do nothing
        // 如果有需要回收的资源，子类自己实现
    }
}
