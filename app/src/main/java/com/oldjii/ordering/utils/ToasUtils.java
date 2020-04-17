package com.oldjii.ordering.utils;

import android.widget.Toast;

public class ToasUtils {
    private static Toast mToast;

    private ToasUtils() {
    }

    public static void showToastMessage(String message) {
        if (mToast == null) {
            synchronized (ToasUtils.class) {
                mToast = Toast.makeText(UIUtils.getContext(), message, Toast.LENGTH_SHORT);
            }
        }
        mToast.setText(message);
        mToast.show();
    }
}
