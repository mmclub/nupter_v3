package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
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
    private final static int ERR_NET = 0;
    private final static int ERR_GETMSG = -1;
    private final static int ERR_PASS = -2;
    private final static int ERR_USER = -3;
    private final static int MSG_SCHOOLCARD = 1;

    private String cookie = "";
    private String postData;
    private Button login;
    private ProgressDialog progressDialog;
    private HttpURLConnection loginConnection;
    private String login_url = "http://my.njupt.edu.cn/ccs/main.login.do";
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_schoolcard);
        login=(Button)findViewById(R.id.login_schoolCard);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginSchoolcardActivity.this);
                progressDialog.setMessage("正在登陆中。。。");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                postData="email=B11040916&password=282155";
                new Login().start();
            }
        });
    }
    class Login extends Thread {
        public void run(){
            try {
                URL loginUrl = new URL(login_url);
                loginConnection = (HttpURLConnection) loginUrl.openConnection();
                loginConnection.setDoOutput(true);
                loginConnection.setDoInput(true);
                loginConnection.setRequestMethod("POST");
                loginConnection.setRequestProperty("Accept-Encoding","gzip");
                loginConnection.setRequestProperty("Accept-Language","zh-CN");
                loginConnection.setRequestProperty("Accept-Charset","GBK,utf-8");
                loginConnection.setConnectTimeout(10000);
                loginConnection.setReadTimeout(10000);
                loginConnection.setUseCaches(false);
                loginConnection.connect();
                DataOutputStream out = new DataOutputStream(loginConnection.getOutputStream());
                out.writeBytes(postData);
                out.flush();
                out.close();
                String key = "";
                if (loginConnection != null) {
                    for (int i = 1; (key = loginConnection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookie = loginConnection.getHeaderField(key);
                            Log.i("string", cookie+"#############");
                            cookie = cookie.substring(0,
                                    cookie.indexOf(";"));
                        }
                    }
                }
                Log.i("string", "#############");
                InputStream inputStream = loginConnection.getInputStream();
                Log.i("string", "#############");
                getHtmlString(inputStream);
                Log.i("string", getHtmlString(inputStream)+"#############");
                inputStream.close();
                loginConnection.disconnect();
                flaghandler.sendEmptyMessage(MSG_SCHOOLCARD);
            }catch (Exception e){
                flaghandler.sendEmptyMessage(ERR_NET);
            }
        }
    }
    public String getHtmlString(InputStream inputStream) {
        byte[] buffer = new byte[1024];
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        String html = "";
        try {
            int bufferSize;
            while ((bufferSize=inputStream.read(buffer)) != -1) {
                outSteam.write(buffer,0,bufferSize);
            }
            html=new String(outSteam.toByteArray(),"utf-8");
            outSteam.close();
        } catch (Exception e) {
            flaghandler.sendEmptyMessage(ERR_GETMSG);
        }
        return html;
    }
    Handler flaghandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_SCHOOLCARD) {
                Toast.makeText(LoginSchoolcardActivity.this, "登陆成功", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            }else if (msg.what == ERR_NET) {
                Toast.makeText(LoginSchoolcardActivity.this, "网络出错了,请检查网络连接", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (msg.what == ERR_GETMSG) {
                Toast.makeText(LoginSchoolcardActivity.this, "读取数据失败，请重新登陆", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (msg.what == ERR_PASS) {
                progressDialog.dismiss();
                Toast.makeText(LoginSchoolcardActivity.this, "密码错误！！", Toast.LENGTH_LONG).show();
            } else if (msg.what == ERR_USER) {
                progressDialog.dismiss();
                Toast.makeText(LoginSchoolcardActivity.this, "用户名不存在或未按照要求参加教学活动！！", Toast.LENGTH_LONG).show();
            }
        }
    };
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
