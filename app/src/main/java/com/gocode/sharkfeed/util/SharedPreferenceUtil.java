package com.gocode.sharkfeed.util;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

import com.gocode.sharkfeed.Constants;

public class SharedPreferenceUtil {
    public static boolean putPageIndex(@NonNull Context context, int value) {
        return putIntExtra(context, Constants.CURRENT_PAGE_INDEX, value);
    }
    public static int getPageIndex(@NonNull Context context) {
        return getIntExtra(context, Constants.CURRENT_PAGE_INDEX);
    }
    private static boolean putIntExtra(@NonNull Context context, String key, int value) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SP_PI, Context.MODE_PRIVATE);
        return preferences.edit().putInt(key, value).commit();
    }

    private static int getIntExtra(@NonNull Context context, String key) {
        SharedPreferences preferences = context.getSharedPreferences(
                Constants.SP_PI, Context.MODE_PRIVATE);
        return preferences.getInt(key, 1);
    }
}
