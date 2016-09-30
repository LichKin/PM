package com.ricky.pm.utils;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceActivity;

/**
 * Created by Administrator on 2015/11/4.
 */
public class SharedPreferenceUtil {

    private static SharedPreferences readablePreferences;
    public static final String PREFIX_STR = "pm_cache";
    public static final String LASTUPDATE_STR = "lastupdate";

    /**
     * 在初始化时调用此方法
     * @param context
     */
    public static void initCptPrefrences(Context context){
        readablePreferences = context.getSharedPreferences(PREFIX_STR, PreferenceActivity.MODE_PRIVATE);
    }


    /**
     * 缓存最后的同步时间
     * @param time
     */
    public static void storeUpdateTime(String time){
        SharedPreferences.Editor editor = readablePreferences.edit();
        editor.putString(LASTUPDATE_STR,time);
        editor.commit();
    }

    /**
     * 获取缓存的最后同步时间
     * @return
     */
    public static String getUpdateTime(){
        if (readablePreferences==null){
            return "";
        }
        return readablePreferences.getString(LASTUPDATE_STR,"");
    }

    public static SharedPreferences getPMPrefrences(){
        return readablePreferences;
    }
}
