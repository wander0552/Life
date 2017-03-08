package com.wander.life.ui.widget;

import android.content.Context;
import android.os.Build;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.widget.EditText;

/**
 * Created by wander on 2017/2/26.
 */

public class MyEditText extends EditText {
    public MyEditText(Context context) {
        this(context,null);
    }

    public MyEditText(Context context, AttributeSet attrs) {
        this(context, attrs,0);
    }

    public MyEditText(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }


    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN){
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.JELLY_BEAN) {
                if (!isCursorVisible())
                    setCursorVisible(true);
            }else {
                setCursorVisible(true);
            }
        }
        return super.onTouchEvent(event);
    }

    public void editComplete(){
        setCursorVisible(false);
    }
}
