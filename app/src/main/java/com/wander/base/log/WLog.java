package com.wander.base.log;


import com.wander.base.dir.DirsUtils;

public final class WLog {
    private static boolean sDebug = true;// 是否记录日志
    private static BaseLogger sLogger = null;

    public static BaseLogger init(String name) {
        if (sLogger == null) {
            String path = DirsUtils.getDir(DirsUtils.LOG) + name + ".log";
            sLogger = BaseLogger.getLogger(path);
        }
        return sLogger;
    }

    public static BaseLogger getLogger() {
        return sLogger;
    }

    public static synchronized void debug(boolean debug) {
        sDebug = debug;
    }

    public static boolean isDebug() {
        return sDebug;
    }

    public static synchronized void trace(boolean isOn) {
        if (isOn) {
            sLogger.traceOn();
        } else {
            sLogger.traceOff();
        }
    }

    public static void d(String tag, String msg) {
        if (sDebug && sLogger != null) {
            sLogger.d(tag, msg);
        }
    }

    public static void i(String tag, String msg) {
        if (sDebug && sLogger != null) {
            sLogger.i(tag, msg);
        }
    }

    public static void e(String tag, String msg) {
        if (sDebug && sLogger != null) {
            sLogger.e(tag, msg);
        }
    }

    public static void e(String msg) {
        if (sDebug && sLogger != null) {
            sLogger.e("test_log", msg);
        }
    }

    public static void e(String tag, Throwable e) {
        if (sDebug && sLogger != null) {
            sLogger.e(tag, e);
        }
    }

    public static void v(String tag, String msg) {
        if (sDebug && sLogger != null) {
            sLogger.v(tag, msg);
        }
    }

    public static void w(String tag, String msg) {
        if (sDebug && sLogger != null) {
            sLogger.w(tag, msg);
        }
    }

    public static void printStackTrace(Exception e) {
        if (sDebug && sLogger != null) {
            sLogger.e("Exception", e.toString());
        }
    }
}
