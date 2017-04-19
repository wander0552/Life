package com.wander.life.widget.recycler.pullRefresh;

/**
 * Created by wander on 2017/4/14.
 */

public interface IRefreshLayout {
    void getVisibleHeight();
    void refreshComplete();
    void oMove(float delta);
    void releaseAction();
}
