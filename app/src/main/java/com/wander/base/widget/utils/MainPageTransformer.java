package com.wander.base.widget.utils;

import android.support.v4.view.ViewCompat;
import android.support.v4.view.ViewPager;
import android.view.View;

/**
 * Created by wander on 2017/3/23.
 * viewPager请勿设置PagerMargin
 * 在item中添加
 */

public class MainPageTransformer implements ViewPager.PageTransformer {
    private final float SCALE_MAX = 0.9f;
    private final float ALPHA_MAX = 1.0f;
    private float scaleMax = SCALE_MAX;
    private float alphaMax = ALPHA_MAX;

    public MainPageTransformer() {
    }

    public MainPageTransformer(float scaleMax, float alphaMax) {
        this.scaleMax = scaleMax;
        this.alphaMax = alphaMax;
    }

    @Override
    public void transformPage(View page, float position) {
        float scale = (position < 0)
                ? ((1 - scaleMax) * position + 1)
                : ((scaleMax - 1) * position + 1);
        float alpha = (position < 0)
                ? ((1 - alphaMax) * position + 1)
                : ((alphaMax - 1) * position + 1);
        //为了滑动过程中，page间距不变，这里做了处理
        if (position < 0) {
            ViewCompat.setPivotX(page, page.getWidth());
            ViewCompat.setPivotY(page, page.getHeight() / 2);
        } else {
            ViewCompat.setPivotX(page, 0);
            ViewCompat.setPivotY(page, page.getHeight() / 2);
        }
        ViewCompat.setScaleX(page, scale);
        ViewCompat.setScaleY(page, scale);
        ViewCompat.setAlpha(page, Math.abs(alpha));
    }

    public void setScaleMax(float scaleMax) {
        this.scaleMax = scaleMax;
    }

    public void setAlphaMax(float alphaMax) {
        this.alphaMax = alphaMax;
    }
}
