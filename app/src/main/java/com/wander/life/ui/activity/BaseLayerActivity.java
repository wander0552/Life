package com.wander.life.ui.activity;

import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.RelativeLayout;

import com.wander.life.R;


/**
 * Created by wander on 2016/11/24.
 */

public class BaseLayerActivity extends BaseActivity {
    private FrameLayout mAboveContainer;// 用于引导图实现
    private FrameLayout mContentContainer;// 用于显示内容
    private FrameLayout mStateViewContainer;// 用于显示各种加载状态的内容


    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.base_view_contain);
        mAboveContainer = (FrameLayout) findViewById(R.id.above_container);
        mContentContainer = (FrameLayout) findViewById(R.id.content_container);
        mStateViewContainer = (FrameLayout) findViewById(R.id.stateView_container);

        FrameLayout titleContainer = (FrameLayout) findViewById(R.id.bar_container);
        if (isUseTitleView()) {
            View titleView = onCreateTitleView(titleContainer);
            if (titleView == null) {
                titleContainer.setVisibility(View.GONE);
            } else {
                titleContainer.setVisibility(View.VISIBLE);
            }
        } else {
            titleContainer.setVisibility(View.GONE);
        }

        if (getLayoutId() > 0) {
            LayoutInflater.from(mContext).inflate(getLayoutId(), getContentContainer());
        }
        setBaseBackground(null);

    }

    /**
     * 重写此方法来修改特定的背景
     *
     * @param drawable
     */
    protected void setBaseBackground(Drawable drawable) {
        if (drawable == null) {
//            baseBg.setBackgroundResource(R.mipmap.bg);
        } else {
            RelativeLayout baseBg = (RelativeLayout) findViewById(R.id.base_bg);
            baseBg.setBackgroundDrawable(drawable);
        }
    }


    protected boolean isUseTitleView() {
        return false;
    }

    protected View onCreateTitleView(ViewGroup container) {
        View view = LayoutInflater.from(mContext).inflate(R.layout.bar_layout, container);
        return view;
    }



    /**
     * 添加布局内容
     * not null
     *
     * @return
     */
    protected int getLayoutId() {
        return 0;
    }


    @Override
    protected void onDestroy() {
        super.onDestroy();
    }


    public FrameLayout getAboveContainer() {
        return mAboveContainer;
    }


    public FrameLayout getContentContainer() {
        return mContentContainer;
    }


    public FrameLayout getStateViewContainer() {
        return mStateViewContainer;
    }

}
