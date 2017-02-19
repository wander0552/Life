package com.wander.life.mod.push;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;

public class PushReStartReceiver extends BroadcastReceiver {
    public PushReStartReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        try {
            context.startService(new Intent(context,PushService.class));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
