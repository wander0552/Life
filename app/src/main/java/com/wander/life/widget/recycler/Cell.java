package com.wander.life.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.view.ViewGroup;

/**
 * Created by wander on 2017/2/23.
 */

public interface Cell {
    /**
     * 回收资源
     *
     */
    void releaseResource();

    /**
     * 获取viewType
     * @return
     */
    int getItemType();

    /**
     * 创建ViewHolder
     * @param parent
     * @param viewType
     * @return
     */
    RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType);

    /**
     * 数据绑定
     * @param holder
     * @param position
     */
    void onBindViewHolder(RVBaseViewHolder holder, int position);
}