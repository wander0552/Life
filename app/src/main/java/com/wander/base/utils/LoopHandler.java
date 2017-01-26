package com.wander.base.utils;

import android.os.Handler;
import android.os.Looper;
import android.os.Message;

/**
 * Created on 2016/6/22.
 */
public class LoopHandler extends Handler {
    private Runnable mRun;
    public boolean isLoop;
    public int delayMillis = 100;

    public LoopHandler() {
    }

    public LoopHandler(Looper looper) {
        super(looper);
    }

    public void dispatchMessage(Message msg) {
        super.dispatchMessage(msg);
        if(this.isLoop) {
            this.mRun.run();
            this.sendEmptyMessageDelayed(0, (long)this.delayMillis);
        }
    }

    public void start(Runnable run) {
        this.mRun = run;
        this.isLoop = true;
        this.sendEmptyMessage(0);
    }

    public void stop() {
        this.isLoop = false;
        this.removeCallbacks(this.mRun);
    }
}
