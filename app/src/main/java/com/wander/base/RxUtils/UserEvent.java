package com.wander.base.RxUtils;

/**
 * Created by wander on 2016/10/31.
 */

public class UserEvent {




    long id;
    String name;

    public UserEvent(long id, String name) {
        this.id = id;
        this.name = name;
    }

    public long getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
