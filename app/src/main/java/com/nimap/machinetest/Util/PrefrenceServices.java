package com.nimap.machinetest.Util;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;


public class PrefrenceServices {

    private static final String HomeData = "OFFLINE_DATA";

    private static final String PREFS_NAME = "MachineTest";
    @SuppressLint("StaticFieldLeak")
    private static PrefrenceServices mSingleton = new PrefrenceServices();
    @SuppressLint("StaticFieldLeak")
    private static Context mContext;

    private PrefrenceServices() {
    }

    public static PrefrenceServices getInstance() {
        return mSingleton;
    }

    public static void init(Context context) {
        mContext = context;
    }

    private SharedPreferences getPrefs() {
        return mContext.getSharedPreferences(PREFS_NAME, Context.MODE_PRIVATE);
    }


    public  String getHomePageData() {
        return getPrefs().getString(HomeData, "");
    }

    public void setHomePageDateData(String data) {
        SharedPreferences.Editor editor = getPrefs().edit();
        editor.putString(HomeData, data);
        editor.apply();
    }


}


