package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import com.umeng.analytics.MobclickAgent;
import org.nupter.nupter.R;

import java.io.ByteArrayOutputStream;
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
    private Button login;
    private HttpURLConnection getCookieConnection,loginConnection;
    private String getCookie_Url= "http://my.njupt.edu.cn/ccs/main/loginIndex.do";
    private String login_url = "http://my.njupt.edu.cn/ccs/ehome/index.do";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_schoolcard);
        new GetCookie().start();
        login=(Button)findViewById(R.id.login_schoolCard);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                postData="__utma=27064737.1965296023.1377618080.1378607553.1378833023.7; __utmb=27064737.3.10.1378833023; __utmc=27064737;__utmz=27064737.1378833023.7.7.utmcsr=njupt.edu.cn|utmccn=(referral)|utmcmd=referral|utmcct=/email=B11040916&password=282155";
                new Login().start();
            }
        });
    }
    class GetCookie extends Thread {
        public void run() {
            try {
                //得到cookie
                URL loginUrl = new URL(getCookie_Url);
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
                Toast.makeText(LoginSchoolcardActivity.this, "网络出错了,请检查网络连接", Toast.LENGTH_LONG).show();
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
                loginConnection.setRequestProperty("Referer", "http://my.njupt.edu.cn/ccs/main/loginIndex.do");
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
                Log.i("TAG", getHtmlString(inPost));
            }catch (Exception e){
                Toast.makeText(LoginSchoolcardActivity.this, "网络出错了,请检查网络连接", Toast.LENGTH_LONG).show();
            }
        }
    }
    public String getHtmlString(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        String html = "";
        try {
            int j = 0;
            int bufferSize;
            while ((bufferSize=inputStream.read(buffer)) != -1) {
                outSteam.write(buffer,0,bufferSize);
            }
            html=new String(outSteam.toByteArray(),"utf-8");
            outSteam.close();
        } catch (Exception e) {
            Toast.makeText(LoginSchoolcardActivity.this, "读取数据失败，请重新登陆", Toast.LENGTH_LONG).show();
        }
        return html;
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
