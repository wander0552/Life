package com.wander.life.database;

import android.database.Cursor;
import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;

import com.wander.base.log.WLog;
import com.wander.life.bean.Letter;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wander on 2017/2/5.
 */

public class DbUtils {
    static String TAG = "DbUtils";

    public static boolean saveLetter(Letter letter) {
        if (letter == null) {
            return false;
        }
        try {
            SQLiteDatabase database = DataBaseHelper.getInstance().getWritableDatabase();
            long ret = database.insert(DataBaseHelper.LETTER_TABLE, null, letter.getContentValues());
            if (ret == -1) {
                WLog.e(TAG, "saveMusic(error): insert " + letter.getFromName());
                return false;
            } else if (ret > -1) {
                letter.setId(ret);
            }
            return true;
        } catch (SQLException e) {
            WLog.printStackTrace(e);
            return false;
        }
    }

    /**
     * 分页查询
     *
     * @param typeId
     * @param pageNum
     * @param pageSize
     * @return
     */
    public static List<Letter> listLetter(int typeId, int pageNum, int pageSize) {
        List<Letter> list = new ArrayList<>();
        Cursor cursor = null;
        try {
            String where = "type = ?";
            String[] whereValue = {Long.toString(typeId)};
            cursor = DataBaseHelper.getInstance().getWritableDatabase().query(DataBaseHelper.LETTER_TABLE, null, where, whereValue, null, null, "createtime asc ", pageNum * pageSize + "," + pageSize);
            while (null != cursor && cursor.moveToNext()) {
                Letter m = new Letter();
                if (m.getInfoFromDataBase(cursor)) {
                    list.add(m);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            WLog.d(TAG, "loadMusic:" + e.getMessage());
        } finally {
            if (null != cursor && !cursor.isClosed()) {
                cursor.close();
            }
        }
        return list;
    }




}
