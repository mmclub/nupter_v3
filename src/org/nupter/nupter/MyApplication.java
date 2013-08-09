package org.nupter.nupter;

import com.baidu.mapapi.BMapManager;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.nostra13.universalimageloader.core.ImageLoader;
import com.nostra13.universalimageloader.core.ImageLoaderConfiguration;
import com.orm.SugarApp;
import com.parse.Parse;
import com.parse.ParseInstallation;
import com.parse.PushService;
import org.nupter.nupter.activity.MainActivity;
import org.nupter.nupter.activity.MessageListActivity;


/**
 * 应用的Application类
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class MyApplication extends SugarApp {

    public static final boolean IS_DEBUG = true;

    private static Context context;
    private static MyApplication instance;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;

    public static final String BaiduMapKey = "5C9714D31C7957714ED1061078A44CFFCE840A86";
    public static final String ParseAppID = "upYCcciwbHWM2dydAjY5rTuTX8u4fNZ7bKidgVrU";
    public static final String ParseClientKey = "JDTv72oswGvJ9hTZEAb4GdVy29qOxzhaC4YqqZdN";

    public void onCreate() {
        super.onCreate();
        MyApplication.context = getApplicationContext();
        instance = this;

        // 初始化Parse SDK
        Parse.initialize(this, ParseAppID, ParseClientKey);
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
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(BaiduMapKey, new MyGeneralListener())) {
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
                MyApplication.instance.m_bKeyRight = false;
            }
        }
    }

}
