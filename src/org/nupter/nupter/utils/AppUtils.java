package org.nupter.nupter.utils;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager.NameNotFoundException;
import org.nupter.nupter.MyApplication;




/**
 * App信息辅助类
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */

public class AppUtils {

    public static String getVersionName(Context context) {
        /**
         * 获取版本名称
         */
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionName;
        } catch (NameNotFoundException e) {
            return "";
        }
    }

    public static int getVersionCode(Context context) {
        /**
         * 获取版本号
         */
        try {

            PackageInfo info = context.getPackageManager().getPackageInfo(context.getPackageName(),
                    0);
            return info.versionCode;
        } catch (NameNotFoundException e) {
            return 0;
        }
    }

    public static MyApplication getMyApplication(Context context) {
        return (MyApplication) context.getApplicationContext();
    }
}