package com.wander.base.net;

import android.text.TextUtils;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 * 解析工具类
 */
public class ParseUtils {

    /**
     * Code返回码
     *
     * @param json
     * @return code
     */
    public static int parseCode(String json) {// 获取返回状态码code
        try {
            JSONObject js = new JSONObject(json);
            String code = js.getString("code");
            return TextUtils.isEmpty(code) ? -1 : Integer.parseInt(code);
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return -1;
    }

    /**
     * alert
     *
     * @param json
     * @return code
     */
    public static String parseAlert(String json) {// 获取返回状态码code
        try {
            JSONObject js = new JSONObject(json);
            return js.getString("alert");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Message
     *
     * @param json
     * @return code
     */
    public static String parseMessage(String json) {// 获取返回状态码code
        try {
            JSONObject js = new JSONObject(json);
            return js.getString("message");
        } catch (Exception e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
        return null;
    }

    /**
     * Object
     * @param json
     * @return code
     */
    public static <T> T parseResultObj(String json, Class<T> classOfT) {
        try {
            if (classOfT == null) {
                return null;
            }
            JSONObject js = new JSONObject(json);
            String result = js.getString("data");
            if (TextUtils.isEmpty(result))
                return null;
            Gson gson = new Gson();
            T t = gson.fromJson(result, classOfT);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> List<T> toList(String json, Class<T> classOfT) {
        try {
            if (classOfT == null || TextUtils.isEmpty(json)) {
                return null;
            }
            Gson gson = new Gson();
            List<T> list = gson.fromJson(json, new TypeToken<ArrayList<T>>() {}.getType());
            return list;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static <T> T toObject(String json, Class<T> classOfT){
        try {
            if (classOfT == null || TextUtils.isEmpty(json)) {
                return null;
            }
            Gson gson = new Gson();
            T t = gson.fromJson(json, classOfT);
            return t;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }


}
