package org.nupter.nupter.utils;


import org.nupter.nupter.AppConstants;




/**
 * 日志输出类
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 * 只是对android.util.Log.d的简单包装。开发人员在输出日志地时候应当尽量使用这个方法，因为可以通过设置  AppConstants.IS_DEBUG  在编译出给用户的APK禁用日志
 */

public class Log {

    protected static final String TAG = "TAG";

    public static void d(String msg) {
        if (AppConstants.IS_DEBUG)
            android.util.Log.d(TAG, msg);
    }

}