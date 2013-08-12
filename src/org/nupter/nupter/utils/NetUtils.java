package org.nupter.nupter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.nupter.nupter.MyApplication;


/**
 * 网络辅助类
 *
 * @author <a href="mailto:lxyweb@gmail.com、
 * ">Lin xiangyu</a>
 */

public class NetUtils {


    public static boolean isNewworkConnected() {

        /**
         * 检测网络是否连接
         * usage:
         *      if (NetworkUtils.isNewworkConnected()){
         *
         *      } else {
         *
         *      }
        */
        return ((ConnectivityManager) MyApplication.getAppContext().getSystemService(Context.CONNECTIVITY_SERVICE)).getActiveNetworkInfo() != null;

    }

}
