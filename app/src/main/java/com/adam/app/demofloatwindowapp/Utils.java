package com.adam.app.demofloatwindowapp;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

public abstract class Utils {

    private static final String TAG = "FloatingDemo";

    public static void inFo(Object obj, String str) {
        Log.i(TAG, obj.getClass().getSimpleName() + ": " + str);
    }

    public static void inFo(Class<?> clazz, String str) {
        Log.i(TAG, clazz.getSimpleName() + ": " + str);
    }

    public static void showToast(Context context, String str) {
        Toast.makeText(context, str, Toast.LENGTH_SHORT).show();
    }
}
