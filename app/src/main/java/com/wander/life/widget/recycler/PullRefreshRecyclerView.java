package com.wander.life.widget.recycler;

import android.content.Context;
import android.support.annotation.Nullable;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;

/**
 * Created by wander on 2017/3/16.
 */

public class PullRefreshRecyclerView extends RecyclerView {
    /**
     * RecyclerView 最后可见Item在Adapter中的位置
     */
    private int mLastVisiblePosition = -1;
    private float mLastY = -1;
    private OnScrollBottomListener onScrollBottomListener;
    private boolean isBottom;
    private boolean mScrollTop;
    private PullLoadView mPullLoadView;
    private PullRefreshLoadView mPullRefreshLoadView;
    private OnRefreshListener onRefreshListener;
    private boolean isRefresh = false;
    private boolean isLoadMore = false;
    private boolean dampLoadMore = false;

    public PullRefreshRecyclerView(Context context) {
        this(context, null);
    }

    public PullRefreshRecyclerView(Context context, @Nullable AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullRefreshRecyclerView(Context context, @Nullable AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        addOnScrollListener(new OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                if (isLoadMore) {
                    RecyclerView.LayoutManager layoutManager = getLayoutManager();
                    if (layoutManager instanceof LinearLayoutManager) {
                        mLastVisiblePosition = ((LinearLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof GridLayoutManager) {
                        mLastVisiblePosition = ((GridLayoutManager) layoutManager).findLastVisibleItemPosition();
                    } else if (layoutManager instanceof StaggeredGridLayoutManager) {
                        StaggeredGridLayoutManager staggeredGridLayoutManager = (StaggeredGridLayoutManager) layoutManager;
                        int[] lastPositions = new int[staggeredGridLayoutManager.getSpanCount()];
                        staggeredGridLayoutManager.findLastVisibleItemPositions(lastPositions);
                        mLastVisiblePosition = findMax(lastPositions);
                    }
                }
            }
        });

    }

    @Override
    public boolean onTouchEvent(MotionEvent e) {

        if (mLastY == -1) {
            mLastY = e.getRawY();
        }
        switch (e.getAction()) {
            case MotionEvent.ACTION_DOWN:
                mLastY = e.getRawY();
                break;
            case MotionEvent.ACTION_MOVE:
                if (isLoadMore) {
                    if (dampLoadMore) {
                        float deltaY = mLastY - e.getRawY();
                        mLastY = e.getRawY();
                        if (deltaY > 0) {
                            if (isScrollBottom()) {
                                mPullLoadView.onMove(deltaY / 3);
                            }
                        } else {
                            if (mPullLoadView.getVisibleHeight() > 0) {
                                mPullLoadView.onMove(deltaY);
                                return true;
                            }
                        }
                    } else {
                        isScrollBottom();
                    }
                }

                if (isRefresh) {
                    if (isScrollTop()) {
                        float deltaY = e.getRawY() - mLastY;
                        mPullRefreshLoadView.onMove(deltaY / 3);
                        mLastY = e.getRawY();
                        if (mPullRefreshLoadView.getVisibleHeight() > 0 &&
                                mPullRefreshLoadView.getState() < mPullRefreshLoadView.STATE_REFRESHING) {
                            return false;
                        }
                    }
                }

                break;
            default:
            case MotionEvent.ACTION_UP:
            case MotionEvent.ACTION_CANCEL:
            case MotionEvent.ACTION_OUTSIDE:
                mLastY = -1; // reset
                if (dampLoadMore) {
                    if (mPullLoadView.releaseAction()) {
                        if (onScrollBottomListener != null) {
                            onScrollBottomListener.onLoadMore();
                        }
                    }
                }
                if (isRefresh) {
                    if (mPullRefreshLoadView.releaseAction()) {
                        if (onRefreshListener != null) {
                            onRefreshListener.onRefresh();
                        }
                    }
                }
                break;
        }

        return super.onTouchEvent(e);
    }

    /**
     * 现仅支持layoutManager
     *
     * @return
     */
    private boolean isScrollTop() {
        if (getLayoutManager() instanceof LinearLayoutManager &&
                ((LinearLayoutManager) getLayoutManager()).findFirstCompletelyVisibleItemPosition() <= 1) {
            mScrollTop = true;
        } else {
            mScrollTop = false;
        }
        return mScrollTop;
    }

    private boolean isScrollBottom() {
        View firstView = getChildAt(0);
        if (firstView == null) {
            return false;
        }
        int top = firstView.getTop();
        int topEdge = getPaddingTop();
        //判断RecyclerView 的ItemView是否满屏，如果不满一屏，上拉不会触发加载更多
        boolean isFullScreen = top < topEdge;
        RecyclerView.LayoutManager manager = getLayoutManager();
        int itemCount = manager.getItemCount();
        if (mLastVisiblePosition >= itemCount - 1 && isFullScreen) {
            //最后一个Item了
            isBottom = true;
            //阻尼加载 释放时判断
            if (!dampLoadMore && onScrollBottomListener != null) {
                onScrollBottomListener.onLoadMore();
            }
        } else {
            isBottom = false;
        }
        return isBottom;

    }

    /**
     * 默认滑倒最后一个item 开始加载更多
     *
     * @param onScrollBottomListener
     */
    public void setOnScrollBottomListener(OnScrollBottomListener onScrollBottomListener) {
        this.onScrollBottomListener = onScrollBottomListener;
        isLoadMore = true;
    }

    /**
     * 有阻尼效果的加载更多
     * 需要在adapter头部添加 {@link PullLoadView}
     * 默认false
     */
    public void setDampLoadMore() {
        dampLoadMore = true;
        mPullLoadView = new PullLoadView(getContext());
        mPullLoadView.setRecyclerView(this);
    }

    /**
     * 获取组数最大值
     *
     * @param lastPositions
     * @return
     */
    private int findMax(int[] lastPositions) {
        int max = lastPositions[0];
        for (int value : lastPositions) {
            if (value > max) {
                max = value;
            }
        }
        return max;
    }

    public View getLoadView() {
        return mPullLoadView;
    }

    public void loadComplete() {
        mPullLoadView.refreshComplete();
    }

    public interface OnScrollBottomListener {
        void onLoadMore();
    }

    /**
     * 头部显示阻尼效果的刷新控件
     * 需要在adapter头部添加 {@link PullRefreshLoadView}
     *
     * @param onRefreshListener
     */
    public void setOnRefreshListener(OnRefreshListener onRefreshListener) {
        this.onRefreshListener = onRefreshListener;
        isRefresh = true;
        mPullRefreshLoadView = new PullRefreshLoadView(getContext());
    }

    public PullRefreshLoadView getRefreshView() {
        return mPullRefreshLoadView == null ? null : mPullRefreshLoadView;
    }

    public interface OnRefreshListener {
        void onRefresh();
    }

    public void refreshComplete() {
        if (mPullRefreshLoadView != null) {
            mPullRefreshLoadView.refreshComplete();
        }
    }
}
