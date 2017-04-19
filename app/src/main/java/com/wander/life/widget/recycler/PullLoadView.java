package com.wander.life.widget.recycler;

import android.animation.Animator;
import android.animation.ValueAnimator;
import android.content.Context;
import android.util.AttributeSet;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.animation.Animation;
import android.view.animation.RotateAnimation;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.wander.life.R;


/**
 * Created by Administrator on 8/24 0024.
 */
public class PullLoadView extends LinearLayout {

    private Context mContext; //上下文


    private ImageView mArrowIv;  //箭头

    private TextView mStatusTv;  //下拉刷新  释放刷新  刷新完成


    private int mState;     //状态

    private int mHeight;  //高度

    private Animation mArrowDownAnim;//向下动画
    private Animation mArrowUpAnim;  //向上动画

    public final static int STATE_PULL_TO_LOAD = 0;
    public final static int STATE_RELEASE_TO_LOAD = 1;
    public final static int STATE_LOADING = 2;
    public final static int STATE_LOADED = 3;

    public final static String PULL_TO_REFRESH = "上拉访问社区";
    public final static String RELEASE_TO_REFRESH = "释放进入社区";
    public final static String REFRESHING = "正在进入...";
    public final static String REFRESHED = "正在进入...";
    private PullRefreshRecyclerView mRecyclerView;
    private float mBottomPadding;

    public PullLoadView(Context context) {
        this(context, null);
    }

    public PullLoadView(Context context, AttributeSet attrs) {
        this(context, attrs, 0);
    }

    public PullLoadView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
        initView(context);
    }

    private void initView(Context context) {
        mContext = context;
        mState = STATE_PULL_TO_LOAD;
        LayoutInflater.from(getContext()).inflate(
                R.layout.load_bottom, this);
        ViewGroup.LayoutParams params = new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        setLayoutParams(params);
        setPadding(0, 0, 0, 0);

        mArrowIv = (ImageView) findViewById(R.id.iv_arrow);
        mStatusTv = (TextView) findViewById(R.id.tv_load_status);
        mStatusTv.setText(PULL_TO_REFRESH);

        mArrowDownAnim = new RotateAnimation(180.0f, 0.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowDownAnim.setDuration(180);
        mArrowDownAnim.setFillAfter(true);

        mArrowUpAnim = new RotateAnimation(0.0f, 180.0f,
                Animation.RELATIVE_TO_SELF, 0.5f, Animation.RELATIVE_TO_SELF, 0.5f);
        mArrowUpAnim.setDuration(180);
        mArrowUpAnim.setFillAfter(true);

        measure(ViewGroup.LayoutParams.WRAP_CONTENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        mHeight = (int) (getMeasuredHeight() * 1.1);
    }


    public int getState() {
        return mState;
    }

    public float getVisibleHeight() {
        return mBottomPadding;
    }


    public void setPaddingHeight(float height) {
        mBottomPadding += height;
        if (mBottomPadding >= 2 * mHeight) {
            mBottomPadding = 2 * mHeight;
        }
        if (mBottomPadding <= 0) {
            mBottomPadding = 0;
        }
        mRecyclerView.setPadding(0, 0, 0, (int) mBottomPadding);
    }

    public void refreshComplete() {
        setState(STATE_LOADED);
        reset();
    }

    /**
     * @param delta
     */
    public void onMove(float delta) {
        setPaddingHeight(delta);
        if (mState <= STATE_RELEASE_TO_LOAD) {
            if (mBottomPadding >= mHeight) {
                setState(STATE_RELEASE_TO_LOAD);
            } else {
                setState(STATE_PULL_TO_LOAD);
            }
        }
    }

    public boolean releaseAction() {
        boolean isOnRefresh = false;

        if (mBottomPadding >= mHeight && mState < STATE_LOADING) {
            setState(STATE_LOADING);
            isOnRefresh = true;
        }

        smoothScrollTo();

        return isOnRefresh;
    }

    public void resetHeight(){
        if (mBottomPadding ==0){
            return;
        }
        smoothScrollTo();
    }
    public void reset() {
        postDelayed(new Runnable() {
            @Override
            public void run() {
                setState(STATE_PULL_TO_LOAD);
            }
        }, 200);
    }

    /**
     * @param state
     */
    public void setState(int state) {
        if (state == mState)
            return;
        switch (state) {
            case STATE_PULL_TO_LOAD:
                mArrowIv.setVisibility(View.VISIBLE);
                mStatusTv.setText(PULL_TO_REFRESH);
                if (mState == STATE_RELEASE_TO_LOAD) {
                    mArrowIv.startAnimation(mArrowDownAnim);
                } else if (mState == STATE_LOADING) {
                    mArrowIv.clearAnimation();
                }
                break;
            case STATE_RELEASE_TO_LOAD:
                mArrowIv.setVisibility(View.VISIBLE);
                mStatusTv.setText(RELEASE_TO_REFRESH);
                if (mState != STATE_RELEASE_TO_LOAD) {
                    mArrowIv.clearAnimation();
                    mArrowIv.startAnimation(mArrowUpAnim);
                }
                break;
            case STATE_LOADING:
                mArrowIv.clearAnimation();
                mStatusTv.setText(REFRESHING);
                mArrowIv.setVisibility(View.INVISIBLE);
                break;
            case STATE_LOADED:
                mArrowIv.clearAnimation();
                mStatusTv.setText(REFRESHED);
                mArrowIv.setVisibility(View.INVISIBLE);
                break;
            default:
                break;

        }
        mState = state;
    }

    private void smoothScrollTo() {
        ValueAnimator animator = ValueAnimator.ofInt((int) mBottomPadding, 0);
        animator.setDuration(300).start();
        animator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                mRecyclerView.setPadding(0, 0, 0, (Integer) animation.getAnimatedValue());
            }
        });
        animator.addListener(new Animator.AnimatorListener() {
            @Override
            public void onAnimationStart(Animator animation) {

            }

            @Override
            public void onAnimationEnd(Animator animation) {
                mRecyclerView.setPadding(0, 0, 0, 0);
            }

            @Override
            public void onAnimationCancel(Animator animation) {

            }

            @Override
            public void onAnimationRepeat(Animator animation) {

            }
        });
        mBottomPadding = 0;
    }

    public void setRecyclerView(PullRefreshRecyclerView mRecyclerView) {
        this.mRecyclerView = mRecyclerView;
    }
}
