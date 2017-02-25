package com.wander.life.utils;

import android.content.Context;
import android.content.Intent;

import com.wander.life.ui.activity.EditorActivity;
import com.wander.life.ui.activity.WriteActivity;

/**
 * Created by wander on 2017/2/23.
 */

public class JumpUtils {


    public static void goEdit(Context context){
        Intent intent = new Intent(context, EditorActivity.class);
        context.startActivity(intent);
    }

}
