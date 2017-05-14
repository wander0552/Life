package com.wander.life.ui.adapter.cell;


import android.graphics.Bitmap;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.nostra13.universalimageloader.core.ImageLoader;
import com.wander.life.R;
import com.wander.life.bean.LetterItem;
import com.wander.life.utils.ImageLoaderUtils;
import com.wander.life.widget.recycler.RVBaseViewHolder;

/**
 * Created by wander on 2017/2/26.
 */

public class ImageCell extends EditBaseCell {
    public ImageCell(LetterItem letterItem) {
        super(letterItem);
    }

    @Override
    void editComplete() {

    }

    @Override
    public Bitmap screenshot() {
        Bitmap bitmap = null;
        if (mHolder != null) {
//            mHolder.itemView.setDrawingCacheEnabled(true);
//            mHolder.itemView.buildDrawingCache();
//            bitmap = mHolder.itemView.getDrawingCache();
        }
        return bitmap;
    }

    @Override
    public int getHeight(RecyclerView recyclerView) {
        return super.getHeight(recyclerView);
    }

    @Override
    public int getItemType() {
        return LetterItem.IMAGE_TYPE;
    }

    @Override
    public ImageViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        return new ImageViewHolder(LayoutInflater.from(parent.getContext()).inflate(R.layout.item_image_layout, parent, false));
    }

    @Override
    public void onBindViewHolder(RVBaseViewHolder holder, int position) {
        super.onBindViewHolder(holder, position);
        if (holder instanceof ImageViewHolder) {
            if (mData.getImageFile() != null) {
                ImageView imageView = holder.getImageView(R.id.item_image_img);
                ImageLoader.getInstance().displayImage(mData.getImageFile(),imageView, ImageLoaderUtils.getDefaultImage());
            }
        }


    }

    class ImageViewHolder extends RVBaseViewHolder {

        public ImageViewHolder(View itemView) {
            super(itemView);
        }
    }
}
