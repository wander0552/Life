package com.wander.life.mod.push;

import android.content.Context;
import android.content.Intent;

import com.wander.base.log.WLog;
import com.wander.life.mod.utils.IModBase;

/**
 * Created by wander on 2017/2/18.
 */

public class PushManage implements IModBase {
    String TAG = "PushManage";
    @Override
    public void init() {

    }

    @Override
    public void release() {

    }
    // IPushMgr
    public void startPushService(Context packageContext) {

        if(packageContext == null) return;

        try {
            Intent intent = new Intent(packageContext, PushService.class);
//            intent.putExtra("appUid", AppInfo.getAppUid());
            packageContext.startService(intent);
//			PushLog.iPrint(TAG, "startPushService");
            WLog.i(TAG, "startPushService");
        } catch (Exception e) {
            e.printStackTrace();
//			PushLog.iPrint(TAG, "startPushService exception");
            WLog.d(TAG, "startPushService exception");
        }
    }

    public void stopPushService(Context packageContext) {
        try {
            Intent intent = new Intent(packageContext, PushService.class);
//            intent.putExtra(PushDefine.PUSH_CTOS_STOPSERVCE, true);
            packageContext.stopService(intent);
            WLog.i(TAG, "stopPushService");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

}
