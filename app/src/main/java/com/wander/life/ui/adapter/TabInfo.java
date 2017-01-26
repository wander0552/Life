package com.wander.life.ui.adapter;

import android.support.v4.app.Fragment;

/**
 * Created by wander on 2017/1/23.
 */

public class TabInfo {
    private long tagId;
    private String tagName;
    private Fragment fragment;
    private int iconRes;

    public TabInfo(long tagId, String tagName, Fragment fragment, int iconRes) {
        this.tagId = tagId;
        this.tagName = tagName;
        this.fragment = fragment;
        this.iconRes = iconRes;
    }

    public long getTagId() {
        return tagId;
    }

    public String getTagName() {
        return tagName;
    }

    public Fragment getFragment() {
        return fragment;
    }

    public int getIconRes() {
        return iconRes;
    }
}
