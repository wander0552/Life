package com.wander.life.bean;

import android.graphics.Bitmap;

/**
 * Created by wander on 2017/2/23.
 */

public class LetterItem {
    public final static int EDIT_TYPE = 0;
    public final static int IMAGE_TYPE = 1;
    public final static int TITLE_TYPE = 2;
    public final static int EVENT_TYPE = 3;
    public final static int ITEM_TYPE = 4;
    public final static int PAINT_TYPE = 5;
    public final static int SOUND_TYPE = 6;


//    {"state":3,"height":1052,"width":780,"name":"img_20170226_151142_356.jpg"}


    private boolean canEdit = true;
    private String imageFile;
    private String detail;
    private boolean fileUpload = false;
    private int width;
    private int height;

    /**
     * 文字处理信息
     */
    private String rtf;
    private String comment;

    public String getDetail() {
        return detail;
    }

    public void setDetail(String detail) {
        this.detail = detail;
    }

    public String getImageFile() {
        return imageFile;
    }

    public void setImageFile(String imageFile) {
        this.imageFile = imageFile;
    }

    public boolean isCanEdit() {
        return canEdit;
    }

    public void setCanEdit(boolean canEdit) {
        this.canEdit = canEdit;
    }
}
