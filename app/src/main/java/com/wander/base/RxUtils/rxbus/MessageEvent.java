package com.wander.base.RxUtils.rxbus;

/**
 * Created by wander on 2017/3/24.
 * 通知更新使用
 */

public class MessageEvent {
    public int code;
    public String msg;

    public MessageEvent() {
        code = -1;
        msg = "msg error";

    }

    public MessageEvent(int code, String msg) {
        this.code = code;
        this.msg = msg;
    }


}
