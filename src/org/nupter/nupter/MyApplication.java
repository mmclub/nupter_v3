package org.nupter.nupter;

import android.app.Application;
import com.baidu.mapapi.BMapManager;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.activity.MessageListActivity;

import java.net.CookieStore;


/**
 * 应用的Application类
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class MyApplication extends SugarApp {

    private static Context context;
    private static MyApplication instance;
    public boolean isGetRightBaiduMapKey = true;
    public BMapManager baiduMapManager = null;

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        instance = this;

        MobclickAgent.setDebugMode(true);
        // 初始化Parse SDK
        Parse.initialize(this, AppConstants.ParseAppID, AppConstants.ParseClientKey);
        PushService.setDefaultPushCallback(this, MessageListActivity.class);
        ParseInstallation.getCurrentInstallation().saveInBackground();

        // 初始化 ImageLoader
        ImageLoaderConfiguration config = new ImageLoaderConfiguration.Builder(getApplicationContext()).build();
        ImageLoader.getInstance().init(config);
    }


    public static MyApplication getInstance() {
        /**
         *  静态方法获取Application 实例
         *  @see <a href="http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android">这个StackOverFlow问答</a>
         */
        return instance;
    }


    public static Context getAppContext() {
        /**
         *  静态方法获取Application Context
         *  @see <a href="http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android">这个StackOverFlow问答</a>
         */
        return MyApplication.context;
    }



    public void initEngineManager(Context context) {
        /**
         * 百度初始化
         */

        if (baiduMapManager == null) {
            baiduMapManager = new BMapManager(context);
        }

        if (!baiduMapManager.init(AppConstants.BaiduMapKey, new MyGeneralListener())) {
            Toast.makeText(MyApplication.instance.getApplicationContext(),
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }


    public static class MyGeneralListener implements MKGeneralListener {
        /**
         * 百度地图SDK常用事件监听，用来处理通常的网络错误，授权验证错误等
         */

        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(context, "您的网络出错啦！",
                        Toast.LENGTH_LONG).show();
            } else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(context, "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError == MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(context,
                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                MyApplication.instance.isGetRightBaiduMapKey = false;
            }
        }
    }

    //用于存储和读取图书馆的cookie
    public class LibraryCookie extends Application {

        private CookieStore librarytCookies;
        public CookieStore getCookie(){
            return librarytCookies;
        }
        public void setCookie(CookieStore cks){
            librarytCookies = cks;
        }
    }


}
