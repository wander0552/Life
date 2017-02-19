/**
 * Copyright (c) 2005, Inc. All rights reserved.
 */
package com.wander.base.context;

import android.content.Context;
import android.os.Environment;
import android.util.Log;

import com.umeng.analytics.MobclickAgent;
import com.wander.base.dir.DirsUtils;
import com.wander.base.log.WLog;

import java.io.FileWriter;
import java.io.IOException;
import java.lang.Thread.UncaughtExceptionHandler;
import java.util.Date;


public class CrashHandler implements UncaughtExceptionHandler {
    private static final String TAG = "CrashHandler";
    private static CrashHandler handler;
    private Context mContext;

    private CrashHandler() {

    }

    public static synchronized CrashHandler getInstance() {
        if (null == handler) {
            handler = new CrashHandler();
        }
        return handler;
    }

    public void init(Context context) {
        mContext = context;
    }

    @Override
    public void uncaughtException(Thread thread, final Throwable ex) {
        WLog.e(TAG, "程序Crash");
        new Thread(new Runnable() {
            @Override
            public void run() {
                if (Environment.getExternalStorageDirectory() != null) {
                    String crashFileName = DirsUtils.getDir(DirsUtils.LOG) + "crash.log";
                    try {
                        FileWriter fw = new FileWriter(crashFileName, true);
                        fw.write(new Date() + "\n");
                        StackTraceElement[] stackTrace = ex.getStackTrace();
                        fw.write(ex.getMessage() + "\n");
                        for (int i = 0; i < stackTrace.length; i++) {
                            fw.write("file:" + stackTrace[i].getFileName() + " class:"
                                    + stackTrace[i].getClassName() + " method:"
                                    + stackTrace[i].getMethodName() + " line:"
                                    + stackTrace[i].getLineNumber() + "\n");
                        }
                        fw.write("\n");
                        fw.close();
                    } catch (IOException e) {
                        Log.e("crash handler", "load file failed...", e.getCause());
                    }
                }
                ex.printStackTrace();
                MobclickAgent.reportError(mContext, WLog.getLogger().getStackTraceString(ex));
                MobclickAgent.onKillProcess(mContext);
                android.os.Process.killProcess(android.os.Process.myPid());
            }
        }).start();

    }
}
