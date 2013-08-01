package org.nupter.nupter;

import android.app.Application;
import android.content.Context;


/**
 * 应用的Application类
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 */
public class MyApplication extends Application {

    private static Context context;

    public void onCreate(){
        super.onCreate();
        MyApplication.context = getApplicationContext();
    }

    public static Context getAppContext() {

        /**
         *  静态方法获取Application Context
         *  @see <a href="http://stackoverflow.com/questions/2002288/static-way-to-get-context-on-android">这个StackOverFlow问答</a>
         */
        return MyApplication.context;
    }


}
