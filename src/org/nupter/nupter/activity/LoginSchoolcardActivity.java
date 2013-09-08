package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.R;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;

/**
 * Created with IntelliJ IDEA.
 * User: sudongsheng
 * Date: 13-8-31
 * Time: 下午4:04
 * To change this template use File | Settings | File Templates.
 */
public class LoginSchoolcardActivity extends Activity {
    private String cookie = "";
    private String postData;
    private HttpURLConnection getCookieConnection,loginConnection;
    private String login_url = "http://my.njupt.edu.cn/ccs/main/loginIndex.do";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_number_save);
        new GetCookie().start();
        postData="__utmz=27064737.1377937766.4.4.utmcsr=njupt.edu.cn|utmccn=(referral)|utmcmd=referral|utmcct=/email=B11040916&password=282155";
        new Login().start();
    }
    class GetCookie extends Thread {
        public void run() {
            try {
                //得到cookie
                URL loginUrl = new URL(login_url);
                getCookieConnection = (HttpURLConnection) loginUrl.openConnection();
                getCookieConnection.setDoOutput(true);
                getCookieConnection.setDoInput(true);
                getCookieConnection.setRequestMethod("GET");
                getCookieConnection.setConnectTimeout(10000);
                getCookieConnection.setReadTimeout(10000);
                getCookieConnection.setUseCaches(false);
                getCookieConnection.connect();
                String key = "";
                if (getCookieConnection != null) {
                    for (int i = 1; (key = getCookieConnection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookie = getCookieConnection.getHeaderField(key);
                            cookie = cookie.substring(0,
                                    cookie.indexOf(";"));
                        }
                    }
                }
                Log.i("TAG", cookie);
            }catch (Exception e){
            }
        }
    }
    class Login extends Thread {
        public void run(){
            try {
                URL loginUrl = new URL(login_url);
                loginConnection = (HttpURLConnection) loginUrl.openConnection();
                loginConnection.setDoOutput(true);
                loginConnection.setDoInput(true);
                loginConnection.setRequestMethod("POST");
                loginConnection.setRequestProperty("Cookie", cookie);
                loginConnection.setRequestProperty("Accept-Encoding","gzip");
                loginConnection.setRequestProperty("Accept-Language","zh-CN");
                loginConnection.setRequestProperty("Accept-Charset", "utf-8");
                loginConnection.setConnectTimeout(10000);
                loginConnection.setReadTimeout(10000);
                loginConnection.setUseCaches(false);
                loginConnection.connect();
                DataOutputStream out = new DataOutputStream(
                        loginConnection.getOutputStream());
                out.writeBytes(postData);
                out.flush();
                out.close();
                InputStream inPost = loginConnection.getInputStream();
                int chByte1 = 0;
                byte[] bin = new byte[1024];
                chByte1 = inPost.read(bin, 0, 1024);
                String s = "";
                while (chByte1 != -1) {
                    s = new String(bin, 0, chByte1, "gbk");
                    Log.i("TAG",s);
                    chByte1 = inPost.read(bin, 0, 1024);
                }
            }catch (Exception e){

            }
        }
    }

    @Override
    protected void onPause() {
        super.onPause();
        MobclickAgent.onPause(this);
    }

    @Override
    protected void onResume() {
        super.onResume();
        MobclickAgent.onResume(this);
    }

}
