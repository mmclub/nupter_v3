package org.nupter.nupter;

import android.app.Application;
import android.content.Context;
import android.widget.Toast;
import com.baidu.mapapi.BMapManager;
import com.baidu.mapapi.MKGeneralListener;
import com.baidu.mapapi.map.MKEvent;
import com.orm.Database;
import com.orm.SugarApp;
import com.parse.Parse;


/**
 * 应用的Application类
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class MyApplication extends SugarApp {

    private static Context context;
    private static MyApplication instance;
    public boolean m_bKeyRight = true;
    public BMapManager mBMapManager = null;

    public static final String strKey = "5C9714D31C7957714ED1061078A44CFFCE840A86";

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
        Parse.initialize(this, "MqbhaZ0G5tX76IISHjbp4JekaYfyzIXWa0pdKHKv", "HF1zGCXpm5hfuvvCUS6taOmM0WiE6cLbhsUiI8JB");

        instance = this;

    }



    public static MyApplication getInstance(){
        return instance;
    }


    public void initEngineManager(Context context) {
        if (mBMapManager == null) {
            mBMapManager = new BMapManager(context);
        }

        if (!mBMapManager.init(strKey,new MyGeneralListener())) {
            Toast.makeText(MyApplication.instance.getApplicationContext(),
                    "BMapManager  初始化错误!", Toast.LENGTH_LONG).show();
        }
    }



    // 常用事件监听，用来处理通常的网络错误，授权验证错误等
    public  static class MyGeneralListener implements MKGeneralListener {

        @Override
        public void onGetNetworkState(int iError) {
            if (iError == MKEvent.ERROR_NETWORK_CONNECT) {
                Toast.makeText(context, "您的网络出错啦！",
                        Toast.LENGTH_LONG).show();
            }
            else if (iError == MKEvent.ERROR_NETWORK_DATA) {
                Toast.makeText(context, "输入正确的检索条件！",
                        Toast.LENGTH_LONG).show();
            }
            // ...
        }

        @Override
        public void onGetPermissionState(int iError) {
            if (iError ==  MKEvent.ERROR_PERMISSION_DENIED) {
                //授权Key错误：
                Toast.makeText(context,
                        "请在 DemoApplication.java文件输入正确的授权Key！", Toast.LENGTH_LONG).show();
                MyApplication.instance.m_bKeyRight = false;
            }
        }
    }

    public static Context getAppContext() {

        /**
         *  静态方法获取Application Context
         *  @see <a href="http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android">这个StackOverFlow问答</a>
         */
        return MyApplication.context;
    }





}
