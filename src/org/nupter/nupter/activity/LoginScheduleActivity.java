package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.util.Log;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import com.ant.liao.GifView;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.JsoupTest;

import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 课表板块主界面
 * author sudongsheng
 */

public class LoginScheduleActivity extends Activity {
    private final static int ERR_NET = 0;
    private final static int ERR_NET2 = -1;
    private final static int ERR_CHECK = -2;
    private final static int ERR_PASS = -3;
    private final static int ERR_USER = -4;
    private final static int MSG_START = 1;

    private GifView gifView;
    private String cookie = "";
    private String postData = "";
    private HttpURLConnection getCookieConnection, getIdentifyConnection, loginConnection, getTableConnection;
    private InputStream checkCodeInputStream, tableInputStream;
    private EditText username;
    private EditText password;
    private EditText check;
    private Button login;
    private String checkNumber;
    private String userNumber;
    private String passNumber;
    private StringBuffer html;
    private Intent intent;
    private ArrayList<ArrayList<String>> list;
    private JsoupTest jsoupTest;
    private ProgressDialog progressDialog;
    private String login_url = "http://202.119.225.35/default2.aspx";
    private String getTable_url = "http://202.119.225.35/xskbcx.aspx?xh=";
    private String checkCode_url = "http://202.119.225.35/CheckCode.aspx";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_schedule_login);
        getActionBar().setDisplayHomeAsUpEnabled(true);
        intent = getIntent();
        int Jump = intent.getIntExtra("Jump", 0);

        jsoupTest = new JsoupTest();
        username = (EditText) findViewById(R.id.username);
        password = (EditText) findViewById(R.id.password);
        check = (EditText) findViewById(R.id.identify);
        login = (Button) findViewById(R.id.login_schedule);
        gifView = (GifView) findViewById(R.id.gifView);
        new GetCheckCode().start();
        gifView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                new GetCheckCode().start();
            }
        });
        login.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.login_btn_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.login_btn);
                }
                return false;
            }
        });
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginScheduleActivity.this);
                progressDialog.setMessage("正在登陆中。。。");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                userNumber = username.getText().toString();
                passNumber = password.getText().toString();
                checkNumber = check.getText().toString();
                if (userNumber.equals("")) {
                    Toast.makeText(LoginScheduleActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (passNumber.equals("")) {
                    Toast.makeText(LoginScheduleActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    postData = "__VIEWSTATE=dDwtMTg3MTM5OTI5MTs7PmemTdyOgz7iR3IwB6rzBV6MRdNi&TextBox1=" + userNumber + "&TextBox2=" + passNumber + "&TextBox3=" + checkNumber + "&RadioButtonList1=%D1%A7%C9%FA&Button1=&lbLanguage=";
                    new Login().start();
                }
            }
        });
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
        String schedule = preferences.getString("schedule", "null");
        if ((!schedule.equals("null"))&&Jump==0) {
            list = jsoupTest.parse(schedule);
            Intent intent = new Intent(LoginScheduleActivity.this, ScheduleActivity.class);
            Bundle bundle = new Bundle();
            bundle.putStringArrayList("First", list.get(0));
            bundle.putStringArrayList("Third", list.get(1));
            bundle.putStringArrayList("Sixth", list.get(2));
            bundle.putStringArrayList("Eighth", list.get(3));
            bundle.putStringArrayList("Eleventh", list.get(4));
            intent.putExtras(bundle);
            LoginScheduleActivity.this.startActivity(intent);
        }
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
                gifView.setShowDimension(90, 50);
                gifView.setGifImageType(GifView.GifImageType.COVER);
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
                InputStream inPost = loginConnection.getInputStream();
                int chByte1 = 0;
                byte[] bin = new byte[1024];
                chByte1 = inPost.read(bin, 0, 1024);
                String s = "";
                while (chByte1 != -1) {
                    s = new String(bin, 0, chByte1, "gbk");
                    chByte1 = inPost.read(bin, 0, 1024);
                }
                Pattern pattern = Pattern.compile("验证码不正确！！");
                Matcher matcher = pattern.matcher(s);
                if (matcher.find()) {
                    flaghandler.sendEmptyMessage(ERR_CHECK);
                    return;
                }
                Pattern pattern1 = Pattern.compile("密码错误！！");
                Matcher matcher1 = pattern1.matcher(s);
                if (matcher1.find()) {
                    flaghandler.sendEmptyMessage(ERR_PASS);
                    return;
                }
                Pattern pattern2 = Pattern.compile("用户名不存在或未按照要求参加教学活动！！");
                Matcher matcher2 = pattern2.matcher(s);
                if (matcher2.find()) {
                    flaghandler.sendEmptyMessage(ERR_USER);
                    return;
                }
                inPost.close();
                loginConnection.disconnect();
                getTable_url=getTable_url+userNumber;
                getTableUrl = new URL(getTable_url);
                getTableConnection = (HttpURLConnection) getTableUrl.openConnection();
                getTableConnection.setRequestMethod("GET");
                getTableConnection.setRequestProperty("Cookie", cookie);
                getTableConnection.setRequestProperty("Accept-Charset", "gbk");
                getTableConnection.setRequestProperty("Referer", "http://202.119.225.35/xs_main.aspx?xh=" + userNumber);
                getTableConnection.connect();
                tableInputStream = getTableConnection.getInputStream();
                html = new StringBuffer();
                int bufferSize;
                byte[] buffer = new byte[1024];
                bufferSize = tableInputStream.read(buffer, 0, 1024);
                while (bufferSize != -1) {
                    String res = new String(buffer, 0, bufferSize, "gbk");
                    html.append(res);
                    bufferSize = tableInputStream.read(buffer, 0, 1024);
                }
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("schedule", html.toString());
                editor.commit();
                tableInputStream.close();
                getTableConnection.disconnect();
                flaghandler.sendEmptyMessage(MSG_START);
            } catch (IOException e) {
                e.printStackTrace();
                flaghandler.sendEmptyMessage(ERR_NET2);
            }
        }
    }

    Handler flaghandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_START) {
                progressDialog.dismiss();
                list = jsoupTest.parse(html.toString());
                Intent intent = new Intent(LoginScheduleActivity.this, ScheduleActivity.class);
                Bundle bundle = new Bundle();
                bundle.putStringArrayList("First", list.get(0));
                bundle.putStringArrayList("Third", list.get(1));
                bundle.putStringArrayList("Sixth", list.get(2));
                bundle.putStringArrayList("Eighth", list.get(3));
                bundle.putStringArrayList("Eleventh", list.get(4));
                intent.putExtras(bundle);
                LoginScheduleActivity.this.startActivity(intent);
            } else if (msg.what == ERR_NET) {
                Toast.makeText(LoginScheduleActivity.this, "网络出错了", Toast.LENGTH_LONG).show();
            } else if (msg.what == ERR_NET2) {
                Toast.makeText(LoginScheduleActivity.this, "网络出错了", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (msg.what == ERR_CHECK) {
                progressDialog.dismiss();
                Toast.makeText(LoginScheduleActivity.this, "验证码不正确！！", Toast.LENGTH_LONG).show();
                new GetCheckCode().start();
            } else if (msg.what == ERR_PASS) {
                progressDialog.dismiss();
                new GetCheckCode().start();
                Toast.makeText(LoginScheduleActivity.this, "密码错误！！", Toast.LENGTH_LONG).show();
            } else if (msg.what == ERR_USER) {
                progressDialog.dismiss();
                new GetCheckCode().start();
                Toast.makeText(LoginScheduleActivity.this, "用户名不存在或未按照要求参加教学活动！！", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                onBackPressed();
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}