package com.test.zwy.myapp;

import android.content.Context;
import android.content.SharedPreferences;

/**
 * Created by FERO010 on 2016/9/29.
 */

public class helpApi {

    /**
     * 通过版本
     * 判断你是否是第一次打开app
     *
     * @return
     */
    public static boolean isFirstUse() {

        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("myTest", Context.MODE_PRIVATE);
        String firstUse = sharedPreferences.getString("firstUseInfo", "");
        String code = getVersionName() + getVersionCode();

        return !code.equals(firstUse);
    }

    /**
     * 第一次打开设置
     */
    public static void setFirstUseCode() {

        SharedPreferences sharedPreferences = MyApplication.getContext().getSharedPreferences("myTest", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = sharedPreferences.edit();
        String code = getVersionName() + getVersionCode();
        editor.putString("firstUseInfo", code);
        editor.commit();
    }

    public static String getVersionName() {

        return getVersionName(MyApplication.getContext());
    }

    /**
     * 获取versionName
     *
     * @param context
     * @return
     */
    public static String getVersionName(Context context) {

        try {
            String pageName = context.getPackageName();
            String versionName = context.getPackageManager().getPackageInfo(pageName, 0).versionName;
            return versionName;
        } catch (Exception x) {

        }
        return "";

    }

    public static int getVersionCode() {

        return getVersionCode(MyApplication.getContext());
    }

    /**
     * 获取versionCode
     *
     * @param context
     * @return
     */
    public static int getVersionCode(Context context) {

        try {
            String pkName = context.getPackageName();
            int versionCode = context.getPackageManager().getPackageInfo(
                    pkName, 0).versionCode;
            return versionCode;
        } catch (Exception e) {

        }
        return 0;
    }
}
