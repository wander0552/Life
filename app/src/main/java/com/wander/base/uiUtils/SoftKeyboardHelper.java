package com.wander.base.uiUtils;

import android.app.Activity;
import android.graphics.Rect;
import android.view.View;
import android.view.ViewTreeObserver;

/**
 * 用完了要把这个listener放掉, 不然会被observer持有造成泄露并不断执行
 */
public class SoftKeyboardHelper {

    private ViewTreeObserver.OnGlobalLayoutListener globalLayoutListener;
    private View decorView;

    public void observeSoftKeyboard(Activity activity, final OnSoftKeyboardChangeListener listener) {
        decorView = activity.getWindow().getDecorView();
        globalLayoutListener = new ViewTreeObserver.OnGlobalLayoutListener() {
            int previousKeyboardHeight = -1;

            @Override
            public void onGlobalLayout() {
                Rect rect = new Rect();
                decorView.getWindowVisibleDisplayFrame(rect);
                int displayHeight = rect.bottom;
                int height = decorView.getHeight();

                int keyboardHeight = height - displayHeight;
                if (previousKeyboardHeight != keyboardHeight) {
                    boolean hide = (double) displayHeight / height > 0.8;
                    listener.onSoftKeyBoardChange(keyboardHeight, !hide);
                }
                previousKeyboardHeight = height;
            }
        };
        decorView.getViewTreeObserver().addOnGlobalLayoutListener(globalLayoutListener);
    }

    public interface OnSoftKeyboardChangeListener {
        void onSoftKeyBoardChange(int softKeyboardHeight, boolean visible);
    }

    public void releaseListener() {
        if (decorView != null) {
            decorView.getViewTreeObserver().removeGlobalOnLayoutListener(globalLayoutListener);
        }
    }
}
