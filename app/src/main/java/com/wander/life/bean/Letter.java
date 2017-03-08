package com.wander.life.bean;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.SQLException;

import com.wander.base.utils.StringUtils;

import java.io.Serializable;

/**
 * Created by wander on 2017/2/5.
 */

public class Letter implements Serializable {
    /**
     * 草稿
     */
    public static final int LETTER_TYPE_DRAFT = 1;
    public static final int LETTER_TYPE_SEND = 2;
    public static final int LETTER_TYPE_RECEIVE = 3;
    //正在发送中,显示剩余时间
    public static final int LETTER_TYPE_SEND_ON_LINE = 4;
    public static final int LETTER_TYPE_SEND_EMAIL = 5;

    private long id = -1;
    /**
     * 服务器信件id
     */
    private String lId;

    //信件类型，草稿，收信，寄信
    private int type;
    private int uId;
    //加急，节日信
    private int post_type;


    private long createTime;
    /**
     * 解析成对应的item
     */
    private String content = "";
    private String title;
//  书信主题种类
    private int theme = -1;

    private int stampId;
    private String stampPic;
    private int stampValue;
    /**
     * 投递的邮箱
     */
    private int addressId;
    private int toUserId;
    private int fromUserId;

    private String toName;
    private String fromName;
    private String address;
    /**
     * 添加图片描述  上传图片加上letter id
     */
    private String pics = "";
    /**
     * 寄信时间
     */
    private long sendTime;
    /**
     * 收信时间
     */
    private long receiveTime;
    private boolean hasRead;

    private String first_img;
    private String first_record;
//    用户加密,输入用户密码ok
    private int encrypt;
    private int dirty;
    private String reserved1;
    private String reserved2;
    private String reserved3;



    public boolean getInfoFromDataBase(Cursor cursor) {
        if (cursor == null) {
            return false;
        }
        try {
            id = cursor.getInt(cursor.getColumnIndex("id"));
            lId = cursor.getString(cursor.getColumnIndex("lid"));
            type = cursor.getInt(cursor.getColumnIndex("type"));
            post_type = cursor.getInt(cursor.getColumnIndex("post_type"));
            content = cursor.getString(cursor.getColumnIndex("content"));
            title = cursor.getString(cursor.getColumnIndex("title"));
            theme = cursor.getInt(cursor.getColumnIndex("theme"));
            stampId = cursor.getInt(cursor.getColumnIndex("stamp_id"));
            stampPic = cursor.getString(cursor.getColumnIndex("stamp_pic"));
            stampValue = cursor.getInt(cursor.getColumnIndex("stamp_value"));

            toName = cursor.getString(cursor.getColumnIndex("toname"));
            address = cursor.getString(cursor.getColumnIndex("address"));
            addressId = cursor.getInt(cursor.getColumnIndex("address_id"));
            fromName = cursor.getString(cursor.getColumnIndex("fromname"));
            uId = cursor.getInt(cursor.getColumnIndex("uid"));
            toUserId = cursor.getInt(cursor.getColumnIndex("to_userid"));
            fromUserId = cursor.getInt(cursor.getColumnIndex("from_userid"));
            pics = cursor.getString(cursor.getColumnIndex("pics"));
            sendTime = Long.parseLong(cursor.getString(cursor.getColumnIndex("send_time")));
            receiveTime = Long.parseLong(cursor.getString(cursor.getColumnIndex("receive_time")));
            createTime = Long.parseLong(cursor.getString(cursor.getColumnIndex("createtime")));
            hasRead = cursor.getInt(cursor.getColumnIndex("hasread")) == 0;

            first_img= cursor.getString(cursor.getColumnIndex("first_img"));
            first_record= cursor.getString(cursor.getColumnIndex("first_record"));
            encrypt = cursor.getInt(cursor.getColumnIndex("encrypt"));
            dirty = cursor.getInt(cursor.getColumnIndex("dirty"));
            reserved1= cursor.getString(cursor.getColumnIndex("reserved1"));
            reserved2= cursor.getString(cursor.getColumnIndex("reserved2"));
            reserved3= cursor.getString(cursor.getColumnIndex("reserved3"));
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public ContentValues getContentValues() {
        ContentValues cv = new ContentValues();
        cv.put("type", type);

        cv.put("lid", StringUtils.getNotNullString(lId));
        cv.put("post_type",post_type);
        cv.put("content", content);
        cv.put("title", title);
        cv.put("theme",theme);
        cv.put("stamp_id",stampId);
        cv.put("stamp_pic",stampPic);
        cv.put("stamp_value",stampValue);
        cv.put("toname", toName);
        cv.put("fromname", fromName);
        cv.put("address",address);
        cv.put("address_id",addressId);
        cv.put("uid", uId);
        cv.put("to_userid", toUserId);
        cv.put("from_userid", fromUserId);
        //// TODO: 2017/2/5
        cv.put("pics", pics);
        cv.put("send_time", sendTime);
        cv.put("receive_time", receiveTime);
        cv.put("hasread", hasRead ? 0 : 1);
        cv.put("createtime", System.currentTimeMillis());

        cv.put("first_img",first_img);
        cv.put("first_record",first_record);
        cv.put("encrypt",encrypt);
        cv.put("dirty",dirty);
        cv.put("reserved1",reserved1);
        cv.put("reserved2",reserved2);
        cv.put("reserved3",reserved3);
        return cv;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public int getAddressId() {
        return addressId;
    }

    public void setAddressId(int addressId) {
        this.addressId = addressId;
    }

    public int getuId() {
        return uId;
    }

    public void setuId(int uId) {
        this.uId = uId;
    }

    public void setToUserId(int toUserId) {
        this.toUserId = toUserId;
    }

    public void setFromUserId(int fromUserId) {
        this.fromUserId = fromUserId;
    }

    public boolean isHasRead() {
        return hasRead;
    }

    public void setHasRead(boolean hasRead) {
        this.hasRead = hasRead;
    }

    public String getPics() {
        return pics;
    }

    public void setPics(String pics) {
        this.pics = pics;
    }

    public long getSendTime() {
        return sendTime;
    }

    public void setSendTime(long sendTime) {
        this.sendTime = sendTime;
    }

    public long getReceiveTime() {
        return receiveTime;
    }

    public void setReceiveTime(long receiveTime) {
        this.receiveTime = receiveTime;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getlId() {
        return lId;
    }

    public void setlId(String lId) {
        this.lId = lId;
    }

    public int getType() {
        return type;
    }

    public void setType(int type) {
        this.type = type;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getToName() {
        return toName;
    }

    public void setToName(String toName) {
        this.toName = toName;
    }

    public String getFromName() {
        return fromName;
    }

    public void setFromName(String fromName) {
        this.fromName = fromName;
    }

    public int getToUserId() {
        return toUserId;
    }

    public int getFromUserId() {
        return fromUserId;
    }
}
