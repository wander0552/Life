package com.wander.life.widget.recycler;

import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.ViewGroup;

import com.wander.base.log.WLog;

import java.util.ArrayList;
import java.util.List;


/**
 * Created by zhouwei on 17/1/19.
 */

public abstract class RVBaseAdapter<C extends RVBaseCell> extends RecyclerView.Adapter<RVBaseViewHolder> {
    public static final String TAG = "RVBaseAdapter";
    protected List<C> mData;

    public RVBaseAdapter() {
        mData = new ArrayList<>();
    }

    public void setData(List<C> data) {
        addAll(data);
        notifyDataSetChanged();
    }

    public List<C> getData() {
        return mData;
    }

    @Override
    public RVBaseViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        for (int i = 0; i < getItemCount(); i++) {
            if (viewType == mData.get(i).getItemType()) {
                return mData.get(i).onCreateViewHolder(parent, viewType);
            }
        }

        throw new RuntimeException("wrong viewType");
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        mData.get(position).onBindViewHolder(holder, position);
        onViewHolderBound(holder, position);
    }

    @Override
    public void onViewDetachedFromWindow(RVBaseViewHolder holder) {
        super.onViewDetachedFromWindow(holder);
        WLog.e(TAG, "onViewDetachedFromWindow invoke...");
        //释放资源
        int position = holder.getAdapterPosition();
        //越界检查
        if (position < 0 || position >= mData.size()) {
            return;
        }
        mData.get(position).releaseResource();
    }


    @Override
    public int getItemCount() {
        return mData == null ? 0 : mData.size();
    }

    @Override
    public int getItemViewType(int position) {
        return mData.get(position).getItemType();
    }

    /**
     * add one cell
     *
     * @param cell
     */
    public void add(C cell) {
        mData.add(cell);
        int index = mData.indexOf(cell);
        notifyItemChanged(index);
    }

    public void add(int index, C cell) {
        mData.add(index, cell);
        notifyItemChanged(index);
    }

    /**
     * remove a cell
     *
     * @param cell
     */
    public void remove(C cell) {
        int indexOfCell = mData.indexOf(cell);

        remove(indexOfCell);
    }

    public void remove(int index) {
        if (index < 0 || index >= mData.size()) {
            return;
        }
        mData.remove(index);
        notifyItemRemoved(index);
    }

    /**
     * @param start
     * @param count
     */
    public void remove(int start, int count) {
        if ((start + count) > mData.size()) {
            return;
        }

        mData.subList(start, start + count).clear();

        notifyItemRangeRemoved(start, count);
    }


    /**
     * add a cell list
     *
     * @param cells
     */
    public void addAll(List<C> cells) {
        if (cells == null || cells.size() == 0) {
            return;
        }
        WLog.e(TAG, "addAll cell size:" + cells.size());
        mData.addAll(cells);
        notifyItemRangeChanged(mData.size() - cells.size(), mData.size());
    }

    public void addAll(int index, List<C> cells) {
        if (cells == null || cells.size() == 0) {
            return;
        }
        mData.addAll(index, cells);
        notifyItemRangeChanged(index, index + cells.size());
    }

    public void clear() {
        mData.clear();
        notifyDataSetChanged();
    }

    /**
     * 更新制定的cell
     * @param cell
     */
    public void notifyOneCell(Cell cell) {
        int indexOf = mData.indexOf(cell);
        if (indexOf >=0 && indexOf <mData.size()){
            notifyItemChanged(indexOf);
        }
    }

    /**
     * 如果子类需要在onBindViewHolder 回调的时候做的操作可以在这个方法里做
     *
     * @param holder
     * @param position
     */
    protected abstract void onViewHolderBound(RVBaseViewHolder holder, int position);

}
