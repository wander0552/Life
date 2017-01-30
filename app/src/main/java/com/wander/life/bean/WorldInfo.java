package com.wander.life.bean;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wander on 2017/1/29.
 */

public class WorldInfo {
    private int wId;
    private String title;
    /**
     * 详情介绍
     */
    private String des;
    private String address;
    private String pic1;
    private String pic2;
    /**
     * 摘要
     */
    private String summary;
    /**
     * 邮件总数
     */
    private int mailCount;

    /**
     * 评论总数
     */
    private int commentCount;

    private long createTime;

    public static List<WorldInfo> getList(){
        String pic1 = "http://s9.sinaimg.cn/orignal/69db613dgd5775c5712f8&690";
        String pic2 ="http://s3.sinaimg.cn/orignal/48f5e36c5108f6e5af9d2";
        List<WorldInfo> list = new ArrayList<>();
        WorldInfo info1 = new WorldInfo(1000,"查令十字街84号","写信是一个美好的过程。句句寻思，字字落笔，将写好的信装入信封，填地址，贴邮票，旷日费时。等待，期盼。","查令十字街84号"
                ,pic1, "等待，期盼",100,100,System.currentTimeMillis()-100000,pic2);
        list.add(info1);
        list.add(info1);
        list.add(info1);
        list.add(info1);
        list.add(info1);
        list.add(info1);
        list.add(info1);
        list.add(info1);
        return list;

    }

    public WorldInfo(int wId, String title, String des, String address, String pic1, String summary, int mailCount, int commentCount, long createTime, String pic2) {
        this.wId = wId;
        this.title = title;
        this.des = des;
        this.address = address;
        this.pic1 = pic1;
        this.summary = summary;
        this.mailCount = mailCount;
        this.commentCount = commentCount;
        this.createTime = createTime;
        this.pic2 = pic2;
    }

    public int getwId() {
        return wId;
    }

    public void setwId(int wId) {
        this.wId = wId;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getDes() {
        return des;
    }

    public void setDes(String des) {
        this.des = des;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getPic1() {
        return pic1;
    }

    public void setPic1(String pic1) {
        this.pic1 = pic1;
    }

    public String getPic2() {
        return pic2;
    }

    public void setPic2(String pic2) {
        this.pic2 = pic2;
    }

    public String getSummary() {
        return summary;
    }

    public void setSummary(String summary) {
        this.summary = summary;
    }

    public int getMailCount() {
        return mailCount;
    }

    public void setMailCount(int mailCount) {
        this.mailCount = mailCount;
    }

    public int getCommentCount() {
        return commentCount;
    }

    public void setCommentCount(int commentCount) {
        this.commentCount = commentCount;
    }

    public long getCreateTime() {
        return createTime;
    }

    public void setCreateTime(long createTime) {
        this.createTime = createTime;
    }
}
