package com.wander.base.log;

import android.os.Process;
import android.util.Log;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

public class BaseLogger {
    private static Map<String, BaseLogger> sLoggers = null;
    private String mLogFileName = null;
    private int mLevel = INFO;    // 输出到文件时，过滤小于此级别的日志
    protected boolean mTrace = false;    // 打印还是输出到文件

    /**
     * Priority constant for the println method; use Log.v.
     */
    public static final int VERBOSE = 2;

    /**
     * Priority constant for the println method; use Log.d.
     */
    public static final int DEBUG = 3;

    /**
     * Priority constant for the println method; use Log.i.
     */
    public static final int INFO = 4;

    /**
     * Priority constant for the println method; use Log.w.
     */
    public static final int WARN = 5;

    /**
     * Priority constant for the println method; use Log.e.
     */
    public static final int ERROR = 6;

    /**
     * Priority constant for the println method.
     */
    public static final int ASSERT = 7;

    private BaseLogger(final String fileName, int level) {
        this.mLogFileName = fileName;
        this.mLevel = level;
    }

    public static BaseLogger getLogger(final String fileName) {
        return getLogger(fileName, INFO);
    }

    public synchronized static BaseLogger getLogger(final String fileName, int level) {

        if (fileName == null || fileName.length() == 0 || level < VERBOSE || level > ASSERT) {
            throw new IllegalArgumentException("invalid parameter fileName or level");
        }

        BaseLogger logger = null;
        if (sLoggers == null) {
            sLoggers = new HashMap<String, BaseLogger>();
        } else {
            logger = sLoggers.get(fileName);
        }

        if (logger == null) {
            logger = new BaseLogger(fileName, level);
            sLoggers.put(fileName, logger);
        }
        return logger;
    }

    public void traceOff() {
        mTrace = false;
    }

    public void traceOn() {
        mTrace = true;
    }

    public boolean isTracing() {
        return mTrace;
    }

    public void setLevel(int level) {
        this.mLevel = level;
    }

    public int v(String tag, String msg) {
        return mTrace ? Log.v(tag, msg) : println(VERBOSE, tag, msg);
    }

    public int v(String tag, Throwable tr) {
        return v(tag, getStackTraceString(tr));
    }

    public int d(String tag, String msg) {
        return mTrace ? Log.d(tag, msg) : println(DEBUG, tag, msg);
    }

    public int d(String tag, Throwable tr) {
        return d(tag, getStackTraceString(tr));
    }

    public int w(String tag, String msg) {
        return mTrace ? Log.w(tag, msg) : println(WARN, tag, msg);
    }

    public int w(String tag, Throwable tr) {
        return w(tag, getStackTraceString(tr));
    }

    public int i(String tag, String msg) {
        return mTrace ? Log.i(tag, msg) : println(INFO, tag, msg);
    }

    public int i(String tag, Throwable tr) {
        return i(tag, getStackTraceString(tr));
    }

    public int e(String tag, String msg) {
        Log.e(tag, msg);
        return mTrace ? 1 : println(ERROR, tag, msg);
    }

    public int e(String tag, Throwable tr) {
        return e(tag, getStackTraceString(tr));
    }

    public String getStackTraceString(Throwable tr) {
        if (tr == null) {
            return "";
        }
        StringWriter sw = new StringWriter();
        PrintWriter pw = new PrintWriter(sw);
        tr.printStackTrace(pw);
        return sw.toString();
    }

    private final Object mLogLock = new Object();

    private int println(int priority, String tag, String msg) {
        if (priority < mLevel) {
            return 0;
        }

        String[] ps = {"", "", "V", "D", "I", "W", "E", "A"};
        SimpleDateFormat df = new SimpleDateFormat("[MM-dd hh:mm:ss.SSS]");
        String time = df.format(new Date());
        StringBuilder sb = new StringBuilder();
        sb.append(time);
        sb.append("\t");
        sb.append(ps[priority]);
        sb.append("/");
        sb.append(tag);
        int pid = Process.myPid();
        sb.append("(");
        sb.append(pid);
        sb.append("):");
        sb.append(msg);
        sb.append("\n");
        FileWriter writer = null;

        synchronized (mLogLock) {
            try {
                File file = new File(mLogFileName);
                // not exist
                if (!file.exists()) {
                    file.createNewFile();
                }
                writer = new FileWriter(file, true);
                writer.write(sb.toString());
            } catch (FileNotFoundException e) {
                return -1;
            } catch (IOException e) {
                e.printStackTrace();
            } finally {
                if (writer != null) {
                    try {
                        writer.close();
                    } catch (IOException e) {
                    }
                }
            }
        }

        return 0;
    }
}
