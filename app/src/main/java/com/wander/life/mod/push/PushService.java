package com.wander.life.mod.push;

import android.app.Service;
import android.content.Intent;
import android.os.IBinder;

import com.wander.base.log.WLog;

import java.util.Timer;
import java.util.TimerTask;

public class PushService extends Service {
    String Tag = "PushService";
    private Timer timer;

    public PushService() {
    }

    @Override
    public void onCreate() {
        WLog.e(Tag, "-----------service create--------");

    }

    @Override
    public IBinder onBind(Intent intent) {
        return null;
    }

    @Override
    public int onStartCommand(Intent intent, int flags, int startId) {
        super.onStartCommand(intent, flags, startId);

        if (timer == null) {
            timer = new Timer();
            timer.schedule(new TimerTask() {
                @Override
                public void run() {
                    WLog.e(Tag, "running push");
                }
            }, 1000, 600000);
        }
        return Service.START_STICKY;
    }

    @Override
    public boolean onUnbind(Intent intent) {
        return super.onUnbind(intent);

    }
}
