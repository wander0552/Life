package com.wander.life.mod.utils;

/**
 * Created by wander on 2017/2/19.
 */

public interface IModBase {
    //在模块被按需加载的时候自动调用，主线程
    void init();
    void release();
}
