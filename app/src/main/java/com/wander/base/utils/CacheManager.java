package com.wander.base.utils;


import com.wander.base.dir.DirsUtils;

import java.io.File;
import java.io.FileWriter;
import java.io.IOException;


public class CacheManager {
    public static String readFile(File file) throws IOException {
        String res = null;
        try {
            res = FileUtils.fileRead(file.getAbsolutePath(), "utf-8");

        } catch (OutOfMemoryError e) {
        }
        return res;

    }

    public static boolean cacheString(String id, String data) {
        File file = getCacheFile(id);
        try {
            FileWriter fw = new FileWriter(file);
            fw.write(data);
            fw.flush();
            fw.close();
        } catch (Exception e) {
            return false;
        }
        return true;
    }

    public static String loadString(String id, long time) throws IOException {
        File file = getCacheFile(id);
        if (CacheManager.checkCache(time, file, false)) {
            return readFile(file);
        }
        return null;
    }

    public static String loadString(String id) throws IOException {
        File file = getCacheFile(id);
        // 若文件不存在
        if (!file.exists()) {
            return null;
        }
        return readFile(file);
    }

    private static File getCacheFile(String id) {
        return new File(getCachePath(id));
    }

    private static String getCachePath(String id) {
        return DirsUtils.getDir(DirsUtils.CACHE)+id;
    }

    private static boolean expired(File file, long expiredTimeMs) {
        long current = System.currentTimeMillis() / 1000 * 1000 - file.lastModified();
        return (current < 0 || current >= expiredTimeMs);
    }

    public static boolean checkCache(String id, long expiredTimeMs) {
        String path = getCachePath(id);
        return checkCache(expiredTimeMs, path, false);
    }

    public static boolean freshCache(String id) {
        File file = getCacheFile(id);
        return file.setLastModified(System.currentTimeMillis());
    }

    /**
     * 检测缓存是否有效，如果无效，将删除缓存
     *
     * @param expiredTimeMs   缓存时间
     * @param path 缓存文件路径
     * @return 返回检测结果。true表示有效，false表示无效
     */
    public static boolean checkCache(long expiredTimeMs, String path) {
        return checkCache(expiredTimeMs, path, true);
    }

    /**
     * 检测缓存是否有效
     *
     * @param ct      缓存类型
     * @param path    缓存文件路径
     * @param needDel 指示无效时是否删除标识
     * @return true表示有效，false表示无效
     */
    public static boolean checkCache(long expiredTimeMs, String path, boolean needDel) {
        File file = new File(path);
        return checkCache(expiredTimeMs, file, needDel);
    }

    /**
     * 检测缓存是否有效
     *
     * @param file    缓存文件
     * @param needDel 指示无效时是否删除标识
     * @return true表示有效，false表示无效
     */
    public static boolean checkCache(long expiredTimeMs, File file, boolean needDel) {
        // 若文件不存在，则无效
        if (!file.exists()) {
            return false;
        }
        // 有效期检测没超期
        if (!expired(file, expiredTimeMs)) {
            return true;
        }

        if (needDel) {
            file.delete();
        }

        return false;
    }


}
