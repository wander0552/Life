package com.wander.life.database;

import android.database.SQLException;
import android.database.sqlite.SQLiteDatabase;
import android.database.sqlite.SQLiteOpenHelper;

import com.wander.base.log.WLog;
import com.wander.life.App;


/**
 * Created by wander on 2016/11/4.
 */

public class DataBaseHelper extends SQLiteOpenHelper {
    private static final String TAG = "SQLiteOpenHelper";
    //当前数据库版本号
    private static int CURRENT_VERSION = 1;
    public static String DB_FILE_NAME = "life.db";

    private static DataBaseHelper instance = null;
    private SQLiteDatabase db;

    static {
        instance = new DataBaseHelper();
    }
    public static DataBaseHelper getInstance() {
        return instance;
    }

    private DataBaseHelper(){
        super(App.getAppContext().getApplicationContext(),DB_FILE_NAME,null,CURRENT_VERSION);
        db = super.getWritableDatabase();
    }



    @Override
    public void onCreate(SQLiteDatabase db) {
//        execSQL(db,CREATE_LIST_MUSIC);
//        execSQL(db,CREATE_LIST_MUSIC_INDEX);
        execSQL(db, CREATE_LETTER_LIST);
        execSQL(db,CREATE_LETTER_INDEX);

    }

    //列表
    public static final String LETTER_TABLE = "letter";
    private final String CREATE_LETTER_INDEX = "CREATE INDEX IF NOT EXISTS LETTER_INDEX ON letter(type)";
    private static final String CREATE_LETTER_LIST = "CREATE TABLE IF NOT EXISTS [letter] (" +
            "[id] INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[lid] TEXT NOT NULL, " +
            "[type] INTEGER NOT NULL, " +
            "[post_type] INTEGER, " +
            "[content] TEXT, " +
            "[title] TEXT, " +
            "[theme] INTEGER, " +
            "[stamp_id] INTEGER, " +
            "[stamp_pic] TEXT, " +
            "[stamp_value] INTEGER, " +
            "[toname] TEXT, " +
            "[fromname] TEXT, " +
            "[address] TEXT, " +
            "[address_id] INTEGER, " +
            "[uid] INTEGER, " +
            "[to_userid] INTEGER, " +
            "[from_userid] INTEGER, " +
            "[pics] TEXT, " +
            "[send_time] TEXT, " +
            "[receive_time] TEXT, " +
            "[hasread] INTEGER NOT NULL DEFAULT (1)," +
            "[first_img] TEXT, " +
            "[first_record] TEXT, " +
            "[encrypt] INTEGER, " +
            "[dirty] INTEGER, " +
            "[reserved1] TEXT, " +
            "[reserved2] TEXT, " +
            "[reserved3] TEXT, " +
            "[createtime] TEXT)"
            ;

    //列表里的歌曲表
    public static final String MUSIC_TABLE = "music";
    static final String CREATE_LIST_MUSIC_INDEX = "CREATE INDEX IF NOT EXISTS LIST_MUSIC_INDEX ON music(listid)";
    static final String CREATE_LIST_MUSIC = "CREATE TABLE IF NOT EXISTS [music] (" +
            "[id]  INTEGER NOT NULL PRIMARY KEY AUTOINCREMENT, " +
            "[listid] INTEGER NOT NULL, " +
            "[rid] INTEGER, " +
            "[name] TEXT NOT NULL, " +
            "[artist] TEXT NOT NULL, " +
            "[artistid] INTEGER, " +
            "[album] TEXT NOT NULL, " +
            "[duration] INTEGER, " +
            "[hot] INTEGER, " +
            "[source] TEXT NOT NULL, " +
            "[resource] TEXT NOT NULL, " +
            "[hasmv] INTEGER, " +
            "[mvquality] TEXT NOT NULL," +
            "[haskalaok] INTEGER, " +
            "[downsize] INTEGER, " +
            "[downquality] TEXT, " +
            "[filepath] TEXT NOT NULL, " +
            "[fileformat] TEXT NOT NULL, " +
            "[filesize] INTEGER, " +
            "[bkpicurl] TEXT NOT NULL, " +
            "[bkurldate] TEXT, " +
            "[payflag] INTEGER, " +
            "[disable] INTEGER, " +
            "[createtime] TEXT)";


    private void execSQL(SQLiteDatabase db, final String sql) {
        try {
            db.execSQL(sql);
        } catch (SQLException sqle) {
            WLog.printStackTrace(sqle);
        }
    }

    @Override
    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {

    }

    /**
     * 千万不要去关闭它
     */
    @Override
    public synchronized SQLiteDatabase getWritableDatabase() {
        return db;
    }

    /**
     * 千万不要去关闭它
     */
    @Override
    public synchronized SQLiteDatabase getReadableDatabase() {
        return db;
    }
}
