package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ant.liao.GifView;
import org.nupter.nupter.R;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 课表板块主界面
 */

public class ClassTableActivity extends Activity {
    private final static int ERR_NET = -1;
    private final static int ERR_PASS = -2;
    private final static int ERR_SDCARD = -3;
    private final static int MSG_GETSTREAM = 100;
    private final static int MSG_START = 1;

    private GifView gifView;
    private String cookie = "";
    private String postData = "";
    private HttpURLConnection getCookieConnection, getIdentifyConnection, loginConnection, getTableConnection;
    private InputStream checkCodeInputStream,tableInputStream;
    private EditText username;
    private EditText password;
    private EditText check;
    private Button login;
    private Button localLogin;
    private String checkNumber;
    private String userNumber;
    private String passNumber;
    private String login_url = "http://202.119.225.35/default2.aspx";
    private String getTable_url = "http://202.119.225.35/xskbcx.aspx?xh=B11040916";
    private String checkCode_url = "http://202.119.225.35/CheckCode.aspx";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classstable);
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        check = (EditText) findViewById(R.id.identify);
        login = (Button) findViewById(R.id.login);
        localLogin = (Button) findViewById(R.id.localLogin);
        gifView = (GifView) findViewById(R.id.gifView);
        new GetCheckCode().start();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                userNumber = username.getText().toString();
                passNumber = password.getText().toString();
                checkNumber = check.getText().toString();
                postData = "__VIEWSTATE=dDwtMTg3MTM5OTI5MTs7PmemTdyOgz7iR3IwB6rzBV6MRdNi&TextBox1=B11040916&TextBox2=445281199304282155&TextBox3=" + checkNumber + "&RadioButtonList1=%D1%A7%C9%FA&Button1=&lbLanguage=";
                new Login().start();
            }
        });
    }

    class GetCheckCode extends Thread {
        public void run() {
            URL loginUrl;
            try {
                loginUrl = new URL(login_url);
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
                getCookieConnection.disconnect();
                URL getUrl = new URL(checkCode_url);
                getIdentifyConnection = (HttpURLConnection) getUrl.openConnection();
                getIdentifyConnection.setRequestProperty("Cookie", cookie);
                getIdentifyConnection.setRequestProperty("Accept-Charset", "gbk");
                getIdentifyConnection.connect();
                checkCodeInputStream = getIdentifyConnection.getInputStream();
                gifView.setGifImage(checkCodeInputStream);
                gifView.setGifImageType(GifView.GifImageType.COVER);
            } catch (MalformedURLException e) {
                e.printStackTrace();
                flaghandler.sendEmptyMessage(ERR_NET);
            } catch (IOException e) {
                e.printStackTrace();
                flaghandler.sendEmptyMessage(ERR_NET);
            }
        }
    }

    class Login extends Thread {
        URL loginUrl;
        URL getTableUrl;

        public void run() {
            try {
                loginUrl = new URL(login_url);
                loginConnection = (HttpURLConnection) loginUrl.openConnection();
                loginConnection.setDoOutput(true);
                loginConnection.setDoInput(true);
                loginConnection.setRequestMethod("POST");
                loginConnection.setRequestProperty("Cookie", cookie);
                loginConnection.setRequestProperty("Accept-Charset", "gbk");
                loginConnection.setConnectTimeout(10000);
                loginConnection.setReadTimeout(10000);
                loginConnection.setUseCaches(false);
                loginConnection.connect();
                DataOutputStream out = new DataOutputStream(
                        loginConnection.getOutputStream());
                out.writeBytes(postData);
                out.flush();
                out.close();
                int chByte1 = 0;
                byte[] bin = new byte[512];
                InputStream inPost = loginConnection.getInputStream();
                chByte1 = inPost.read(bin);
                String s = new String(bin, 0, chByte1, "gbk");
                Pattern patPost = Pattern.compile("请登录");
                Matcher matPost = patPost.matcher(s);
                if (matPost.find()) {
                    flaghandler.sendEmptyMessage(ERR_PASS);
                }
                inPost.close();
                loginConnection.disconnect();
                getTableUrl = new URL(getTable_url);
                getTableConnection = (HttpURLConnection) getTableUrl.openConnection();
                getTableConnection.setRequestMethod("GET");
                getTableConnection.setRequestProperty("Cookie", cookie);
                getTableConnection.setRequestProperty("Accept-Charset", "gbk");
                getTableConnection.setRequestProperty("Referer","http://202.119.225.35/xs_main.aspx?xh=B11040916");
                getTableConnection.connect();
                tableInputStream = getTableConnection.getInputStream();
                StringBuffer html = new StringBuffer();
                int bufferSize;
                byte[] buffer = new byte[1024];
                bufferSize = tableInputStream.read(buffer, 0, 1024);
                while (bufferSize != -1) {
                    String res = new String(buffer, 0, bufferSize, "gbk");
                    Log.i("TAG", res);
                    html.append(res);
                    bufferSize = tableInputStream.read(buffer, 0, 1024);
                }
                Log.i("str", html.toString());
                tableInputStream.close();
                getTableConnection.disconnect();
            } catch (IOException e) {
                e.printStackTrace();
                flaghandler.sendEmptyMessage(ERR_NET);
            }
        }
    }

    Handler flaghandler = new Handler() {
        public void handleMessage(Message msg) {
/*            if (msg.what == MSG_START) {
                init();
                String res = JsoupTest2.parse(html);
                try {
                    fileService.writeDateFile("timetable.txt", res.getBytes());
                } catch (Exception e) {
                    e.printStackTrace();
                }
                Intent intent = new Intent(Schedule.this, Timetable.class);
                Bundle bundle = new Bundle();
                bundle.putString("html", res);
                intent.putExtras(bundle);
                Schedule.this.startActivity(intent);
            } else if (msg.what == MSG_GETSTREAM) { // 读取网络文件
                html = "";
                try {
                    int buffersize;
                    byte buffer[] = new byte[1024];
                    buffersize = inStream.read(buffer, 0, 1024);
                    while (buffersize != -1) {
                        String res = new String(buffer, 0, buffersize, "gbk");
                        // Log.e("size", String.valueOf(buffersize));
                        // Log.v("html", res);
                        html += res;
                        buffersize = inStream.read(buffer, 0, 1024);
                    }
                    inStream.close();
                    flaghandler.sendEmptyMessage(MSG_START);
                } catch (IOException e) {
                    e.printStackTrace();
                    Log.i("Exception", "IO");
                    flaghandler.sendEmptyMessage(ERR_NET);
                } finally {
                    connection2.disconnect();
                }
            } else */
            if (msg.what == ERR_NET) {
                Toast.makeText(ClassTableActivity.this, "网络出错了", Toast.LENGTH_LONG).show();

            } else if (msg.what == ERR_PASS) {
                init();
                Toast.makeText(ClassTableActivity.this, "登陆失败，请检测你的用户名、密码和验证码", Toast.LENGTH_LONG).show();
            } else if (msg.what == ERR_SDCARD) {
                init();
                Toast.makeText(ClassTableActivity.this, "读写错误！请检测你的sd卡", Toast.LENGTH_LONG).show();
            }

        }
    };

    private void init() {
    }
}