package com.wander.base.dir;

import android.content.Context;
import android.os.Environment;
import android.os.StatFs;
import android.os.storage.StorageManager;
import android.text.TextUtils;

import com.wander.base.log.WLog;
import com.wander.life.App;

import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;


/**
 *  所有得到的路径末尾都已经有了"/"符号了，切记！
 */
public final class DirsUtils {

    /**
     *
     */
    public static final String EXT_READONLY_FLAG = "readonly|";
    // 大于100的不缓存
    public static final int
            SD_ROOT = 0,
            EXT_ROOT = 1, // 外置SD卡
            HOME = 2, // sdcard/Wander
            CACHE = 4, // cache存储数据的位置
            PICS = 5,
            DEFDOWNLOAD = 6,
            DEFEXDOWNLOAD = 7, //外置sd卡默认歌曲下载目录
            CRASH = 8, // 崩溃堆栈文件
            TEMP = 9, // 一个单纯的缓存路径，每次主动退出程序都会清空
            LOG = 10,
            PIC_CACHE = 11,
            SETTING = 12,
            MAX_ID = 51; //保持最大


    /**
     * 所有得到的路径末尾都已经有了"/"符号了，切记！
     *
     * @param dirID
     * @return
     */
    public static String getDir(final int dirID) {
        int savePos = dirID < 100 ? dirID : dirID - 100;
        String dirPath = dirs[savePos];
        if (dirPath != null) {
            return dirPath;
        }
        dirPath = "";
        switch (dirID) {
            case SD_ROOT:
                dirPath = SD_ROOTPATH;
                break;
            case EXT_ROOT:
                dirPath = "";
                List<String> paths = getStoragePaths(App.getAppContext().getApplicationContext());
                if (paths != null && paths.size() >= 2) {
                    for (String path : paths) {
                        if (path != null && !path.equals(getFirstExterPath())) {
                            File pathFile = new File(path);
                            try {
                                String realPath = pathFile.getCanonicalPath();
                                if (realPath.equals(path)) {
                                    dirPath = path;
                                    break;
                                }
                            } catch (IOException e) {
                                e.printStackTrace();
                            }
                        }
                    }
                }
                break;
            case HOME:
                dirPath = HOME_PATH;
                break;
            case CACHE:
                dirPath = HOME_PATH + "cache";
                break;
            case PICS:
                dirPath = HOME_PATH + "pics";
                break;
            case DEFDOWNLOAD:
                dirPath = HOME_PATH + "music";
                break;
            case CRASH:
                dirPath = HOME_PATH + "crash";
                break;
            case TEMP:
                dirPath = HOME_PATH + "temp";
                break;
            case LOG:
                dirPath = HOME_PATH + "log";
                break;
            case DEFEXDOWNLOAD:
                if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.KITKAT) {
                    dirPath = getDir(EXT_ROOT) + "Android/data/" + App.getAppContext().getPackageName();
                } else {
                    dirPath = getDir(EXT_ROOT) + "Wander/music";
                }
                break;
            case PIC_CACHE:
                dirPath = HOME_PATH_FOR_HIDE + "pic_cache";
                break;
            case SETTING:
                dirPath = HOME_PATH_FOR_HIDE + "setting";
                break;
            default:
                break;
        } // switch

        if (!TextUtils.isEmpty(dirPath) && !dirPath.endsWith(File.separator)) {
            dirPath += File.separator;
        }

        if (dirID < 100) {
            dirs[savePos] = dirPath;
        }
        File dir = new File(dirPath);
        if (!dir.exists()) {
            try {
                dir.mkdirs();
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        return dirPath;
    }

    private DirsUtils() {
    }

    private static String[] dirs = new String[MAX_ID];
    /**
     * sd 卡根目录
     */
    private static final String SD_ROOTPATH = Environment
            .getExternalStorageDirectory().getAbsolutePath() + File.separator;
    /**
     * SD卡 应用目录
     */
    private static final String HOME_PATH = SD_ROOTPATH + "Wander" + File.separator;
    private static final String HOME_PATH_FOR_HIDE = HOME_PATH + ".";

    public static String getFirstExterPath() {
        return Environment.getExternalStorageDirectory().getPath();
    }

    public static List<String> getAllExterSdcardPath() {
        List<String> SdList = new ArrayList<String>();

        String firstPath = getFirstExterPath();
        WLog.d("SDCARD", "First externalSdcard:" + firstPath);
        // 得到路径
        try {
            Runtime runtime = Runtime.getRuntime();
            Process proc = runtime.exec("mount");
            InputStream is = proc.getInputStream();
            InputStreamReader isr = new InputStreamReader(is);
            String line;
            BufferedReader br = new BufferedReader(isr);
            while ((line = br.readLine()) != null) {
                WLog.d("SDCARD", line);
                // 将常见的linux分区过滤掉
                if (line.contains("secure"))
                    continue;
                if (line.contains("asec"))
                    continue;
                if (line.startsWith("/data/media"))
                    continue;
                if (line.contains("/mnt/media_rw/") && line.contains("/dev/block/"))
                    continue;
                if (line.contains("system") || line.contains("cache")
                        || line.contains("sys") || line.contains("data")
                        || line.contains("tmpfs") || line.contains("shell")
                        || line.contains("root") || line.contains("acct")
                        || line.contains("proc") || line.contains("misc")
                        || line.contains("obb")) {
                    continue;
                }
                if (line.contains("fat") || line.contains("fuse") || (line
                        .contains("ntfs")) || line.contains("/extSdCard")) {

                    String columns[] = line.split(" ");
                    if (columns != null && columns.length > 1) {
                        String path = columns[1];
                        if (path != null && !SdList.contains(path) && path.toLowerCase().contains("sd"))
                            SdList.add(columns[1]);
                    }
                }
            }
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }

        if (!SdList.contains(firstPath)) {
            SdList.add(firstPath);
        }
        return SdList;
    }

    /**
     * 2015.09.15增加的获取外置sd卡的方法，此方法按android版本使用不同的方式，获取外置sd卡路径
     *
     * @param cxt
     * @return
     */
    public static List<String> getStoragePaths(Context cxt) {
        List<String> pathsList = null;
        if (android.os.Build.VERSION.SDK_INT < android.os.Build.VERSION_CODES.GINGERBREAD) {
            pathsList = getAllExterSdcardPath();
        } else {
            if (cxt == null) {
                return getAllExterSdcardPath();
            }
            pathsList = new ArrayList<String>();
            StorageManager storageManager = (StorageManager) cxt.getSystemService(Context.STORAGE_SERVICE);
            try {
                if (storageManager != null) {
                    Method method = StorageManager.class.getDeclaredMethod("getVolumePaths");
                    if (method != null) {
                        method.setAccessible(true);
                        Object result = method.invoke(storageManager);
                        if (result != null && result instanceof String[]) {
                            String[] pathes = (String[]) result;
                            StatFs statFs;
                            for (String path : pathes) {
                                if (!TextUtils.isEmpty(path) && new File(path).exists()) {
                                    statFs = new StatFs(path);
                                    if (statFs.getBlockCount() * statFs.getBlockSize() != 0) {
                                        pathsList.add(path);
                                    }
                                }
                            }
                        }
                    }
                }
            } catch (Exception e) {
            }
            if (pathsList.size() == 0) {
                pathsList = getAllExterSdcardPath();
            }
        }
        return pathsList;
    }

    public static interface OnSdcardCheckListener {
        public static int REASON_NOSPACE = 11; //没空间可写入
        public static int REASON_USERCANCEL = 12; //用户在修改目录对话框点击取消
        public static int REASON_NOEXIST = 13; //用户所选目录创建失败，不存在目录

        void onSdcardAvailable(boolean isChanged, String newDownPath);

        void onSdcardUnavailable(int reason);
    }

    public static long MIN_SPACE = 100 * 1024 * 1024;  //剩余空间最小限值是32MB

    /**
     * 判断当前下载目录，是否可用，可用就直接调用所传入listener的onSdcardAvailable回调，
     * 先判断默认下载目录，不可以则切换至DEFEXDOWNLOAD，如果还不可用，则执行onSdcardUnavailable并给出原因。
     * onSdcardAvailable 获取 download 路径
     *
     * @param listener
     */
    public static void checkSdcardForDownload(final OnSdcardCheckListener listener) {
        final String defDownPath = getDir(DEFDOWNLOAD);
        final String defExDownPath = getDir(DEFEXDOWNLOAD);
        File downFile = new File(defDownPath);
        if (downFile.exists() && downFile.isDirectory()) {
            if (getPathSpaceSize(defDownPath) > MIN_SPACE) {    //如果剩余空间大于10MB，则直接使用，否则提示用户空间不足
                listener.onSdcardAvailable(false, defDownPath);
            } else {
                //下载目录内存不足提示
                listener.onSdcardUnavailable(OnSdcardCheckListener.REASON_NOSPACE);
            }
        } else {
            //默认目录都不存在，则创建一次
            if (downFile.mkdirs()) {
                if (getPathSpaceSize(defDownPath) > MIN_SPACE) {    //如果剩余空间大于10MB，则直接使用，否则提示用户空间不足
                    listener.onSdcardAvailable(false, defDownPath);
                } else {
                    //下载目录内存不足提示
                    listener.onSdcardUnavailable(OnSdcardCheckListener.REASON_NOSPACE);
                }
            } else {
                //创建目录失败，则直接提示用户目录不可用，判断外存可不可用，可用则弹框一键切换到外存，不存在则直接提示用户目录不可用退出
                boolean isExSdcardCanUse = false;
                if (!TextUtils.isEmpty(defExDownPath)) {
                    File exDownPath = new File(defExDownPath);
                    if (exDownPath.exists() && exDownPath.isDirectory()) {
                        if (android.os.Build.VERSION.SDK_INT > android.os.Build.VERSION_CODES.HONEYCOMB_MR2) {
                            File tmpFile = new File(exDownPath, "tmp.log");
                            try {
                                if (tmpFile.exists()) {
                                    if (tmpFile.delete()) {    //删除成功，则证明目录也是可读写的，直接判断空间
                                        isExSdcardCanUse = true;
                                    }
                                } else {
                                    if (tmpFile.createNewFile()) {
                                        tmpFile.delete();                        //能创建文件，说明下载目录可读写，则执行listener
                                        isExSdcardCanUse = true;
                                    }
                                }
                            } catch (Exception e) {
                            }
                        } else {
                            isExSdcardCanUse = true; //4.4系统以前手机外置sd卡直接使用，不用判断是否要写入
                        }
                    }
                }
                if (isExSdcardCanUse) {
                    String newDownPath = getDir(DEFEXDOWNLOAD);
                    listener.onSdcardAvailable(true, newDownPath);
                } else {
                    listener.onSdcardUnavailable(OnSdcardCheckListener.REASON_NOEXIST);
                }
            }
        }
    }

    public static long getPathSpaceSize(String path) {
        StatFs stat = null;
        try {
            stat = new StatFs(path);
        } catch (Exception e) {
            e.printStackTrace();
            return -1;
        }
        long blockSize = stat.getBlockSize();
        long availableBlocks = stat.getAvailableBlocks();
        long availableSize = availableBlocks * blockSize;
        return availableSize;
    }
}
