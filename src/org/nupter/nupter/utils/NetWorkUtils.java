package org.nupter.nupter.utils;

import android.content.Context;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import org.nupter.nupter.MyApplication;


/**
 * 网络辅助类
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class NetWorkUtils {

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
        ConnectivityManager conMgr = (ConnectivityManager) ((MyApplication.getAppContext()).getSystemService(Context.CONNECTIVITY_SERVICE));

        if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.CONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.CONNECTING) {


            return true;
        } else if (conMgr.getNetworkInfo(0).getState() == NetworkInfo.State.DISCONNECTED
                || conMgr.getNetworkInfo(1).getState() == NetworkInfo.State.DISCONNECTED) {
            return false;

        }
        return false;
    }

}
