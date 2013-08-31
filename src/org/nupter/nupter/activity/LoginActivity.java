package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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

public class LoginActivity extends Activity {
    private final static int ERR_NET = 0;
    private final static int ERR_NET2 = -1;
    private final static int ERR_CHECK = -2;
    private final static int ERR_PASS = -3;
    private final static int ERR_USER = -4;
    private final static int MSG_TABLE = 1;
    private final static int MSG_TEST = 2;

    private GifView gifView;
    private String cookie = "";
    private String postData = "";
    private String postTestData = "";
    private HttpURLConnection getCookieConnection, getIdentifyConnection, loginConnection, getTableConnection, getTestConnection;
    private InputStream checkCodeInputStream, tableInputStream, testInputStream;
    private EditText username;
    private EditText password;
    private EditText check;
    private Button login;
    private String checkNumber;
    private String userNumber;
    private String passNumber;
    private StringBuffer tableHtml, testHtml;
    private Intent intent;
    private String Flag;
    private String testString;
    private JsoupTest jsoupTest;
    private ProgressDialog progressDialog;
    private String login_url = "http://202.119.225.35/default2.aspx";
    private String getTable_url = "http://202.119.225.35/xskbcx.aspx?xh=";
    private String getTest_url = "http://202.119.225.35/xscj_gc.aspx?xh=";
    private String checkCode_url = "http://202.119.225.35/CheckCode.aspx";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        intent = getIntent();
        Flag = intent.getStringExtra("JumpTo");
        getActionBar().setDisplayHomeAsUpEnabled(true);
        jsoupTest=new JsoupTest();
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
                progressDialog = new ProgressDialog(LoginActivity.this);
                progressDialog.setMessage("正在登陆中。。。");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                userNumber = username.getText().toString();
                passNumber = password.getText().toString();
                checkNumber = check.getText().toString();
                if (userNumber.equals("")) {
                    Toast.makeText(LoginActivity.this, "用户名不能为空", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else if (passNumber.equals("")) {
                    Toast.makeText(LoginActivity.this, "密码不能为空", Toast.LENGTH_LONG).show();
                    progressDialog.dismiss();
                } else {
                    postData = "__VIEWSTATE=dDwtMTg3MTM5OTI5MTs7PmemTdyOgz7iR3IwB6rzBV6MRdNi&TextBox1=" + userNumber + "&TextBox2=" + passNumber + "&TextBox3=" + checkNumber + "&RadioButtonList1=%D1%A7%C9%FA&Button1=&lbLanguage=";
                    postTestData = "__VIEWSTATE=dDwtMTU5ODUyNjk2MTt0PHA8bDx4aDs%2BO2w8QjExMDQwOTE2Oz4%2BO2w8aTwxPjs%2BO2w8dDw7bDxpPDE%2BO2k8Mz47aTw1PjtpPDc%2BO2k8OT47aTwxMT47aTwxMz47aTwxNj47aTwyNj47aTwyNz47aTwyOD47aTwzNT47aTwzNz47aTwzOT47aTw0MT47aTw0NT47PjtsPHQ8cDxwPGw8VGV4dDs%2BO2w85a2m5Y%2B377yaQjExMDQwOTE2Oz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDzlp5PlkI3vvJroi4%2FkuJznlJ87Pj47Pjs7Pjt0PHA8cDxsPFRleHQ7PjtsPOWtpumZou%2B8mui9r%2BS7tuWtpumZojs%2BPjs%2BOzs%2BO3Q8cDxwPGw8VGV4dDs%2BO2w85LiT5Lia77yaOz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDzova%2Fku7blt6XnqIvvvIhOSUlU77yJOz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDzooYzmlL%2Fnj63vvJpCMTEwNDA5Oz4%2BOz47Oz47dDxwPHA8bDxUZXh0Oz47bDwyMDExMTUwMjs%2BPjs%2BOzs%2BO3Q8dDw7dDxpPDE0PjtAPFxlOzIwMDEtMjAwMjsyMDAyLTIwMDM7MjAwMy0yMDA0OzIwMDQtMjAwNTsyMDA1LTIwMDY7MjAwNi0yMDA3OzIwMDctMjAwODsyMDA4LTIwMDk7MjAwOS0yMDEwOzIwMTAtMjAxMTsyMDExLTIwMTI7MjAxMi0yMDEzOzIwMTMtMjAxNDs%2BO0A8XGU7MjAwMS0yMDAyOzIwMDItMjAwMzsyMDAzLTIwMDQ7MjAwNC0yMDA1OzIwMDUtMjAwNjsyMDA2LTIwMDc7MjAwNy0yMDA4OzIwMDgtMjAwOTsyMDA5LTIwMTA7MjAxMC0yMDExOzIwMTEtMjAxMjsyMDEyLTIwMTM7MjAxMy0yMDE0Oz4%2BOz47Oz47dDxwPDtwPGw8b25jbGljazs%2BO2w8d2luZG93LnByaW50KClcOzs%2BPj47Oz47dDxwPDtwPGw8b25jbGljazs%2BO2w8d2luZG93LmNsb3NlKClcOzs%2BPj47Oz47dDxwPHA8bDxWaXNpYmxlOz47bDxvPHQ%2BOz4%2BOz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs%2BOzs%2BO3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDw7bDxpPDA%2BO2k8MT47aTwyPjtpPDQ%2BOz47bDx0PDtsPGk8MD47aTwxPjs%2BO2w8dDw7bDxpPDA%2BO2k8MT47PjtsPHQ8QDA8Ozs7Ozs7Ozs7Oz47Oz47dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjs%2BPjt0PDtsPGk8MD47aTwxPjs%2BO2w8dDxAMDw7Ozs7Ozs7Ozs7Pjs7Pjt0PEAwPDs7Ozs7Ozs7Ozs%2BOzs%2BOz4%2BOz4%2BO3Q8O2w8aTwwPjs%2BO2w8dDw7bDxpPDA%2BOz47bDx0PEAwPDs7Ozs7Ozs7Ozs%2BOzs%2BOz4%2BOz4%2BO3Q8O2w8aTwwPjtpPDE%2BOz47bDx0PDtsPGk8MD47PjtsPHQ8QDA8cDxwPGw8VmlzaWJsZTs%2BO2w8bzxmPjs%2BPjs%2BOzs7Ozs7Ozs7Oz47Oz47Pj47dDw7bDxpPDA%2BOz47bDx0PEAwPHA8cDxsPFZpc2libGU7PjtsPG88Zj47Pj47Pjs7Ozs7Ozs7Ozs%2BOzs%2BOz4%2BOz4%2BO3Q8O2w8aTwwPjs%2BO2w8dDw7bDxpPDA%2BOz47bDx0PHA8cDxsPFRleHQ7PjtsPFpKVTs%2BPjs%2BOzs%2BOz4%2BOz4%2BOz4%2BO3Q8QDA8Ozs7Ozs7Ozs7Oz47Oz47Pj47Pj47Pp9OID21POtPt9O28KvUp1SXJHl%2F&ddlXN=&ddlXQ=&Button1=%B0%B4%D1%A7%C6%DA%B2%E9%D1%AF";
                    new Login().start();
                }
            }
        });
    }
    //访问正方主页得到cookie之后，带着cookie访问验证码显示在GifView控件上
    class GetCheckCode extends Thread {
        public void run() {
            URL loginUrl;
            try {
                //得到cookie
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
                //得到验证码的InputStream流并显示在gifView上
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
    //带着cookie，通过post用户名、密码、验证码登陆正方得到网页的InputStream流
    class Login extends Thread {
        URL loginUrl;
        URL getTableUrl;
        URL getTestUrl;

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
                //得到网页的数据后，解析是否登陆进去（这里只取最后一个从0到1024字节的数据，因为登陆信息都放在网页最后）
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

                //设置cookie和其他参数，访问课表网页得到String类型的HTML
                getTable_url = getTable_url + userNumber;
                getTableUrl = new URL(getTable_url);
                getTableConnection = (HttpURLConnection) getTableUrl.openConnection();
                getTableConnection.setRequestMethod("GET");
                getTableConnection.setRequestProperty("Cookie", cookie);
                getTableConnection.setRequestProperty("Accept-Charset", "gbk");
                getTableConnection.setRequestProperty("Referer", "http://202.119.225.35/xs_main.aspx?xh=" + userNumber);
                getTableConnection.connect();
                tableInputStream = getTableConnection.getInputStream();
                tableHtml = new StringBuffer();
                int bufferSize;
                byte[] buffer = new byte[1024];
                bufferSize = tableInputStream.read(buffer, 0, 1024);
                while (bufferSize != -1) {
                    String res = new String(buffer, 0, bufferSize, "gbk");
                    tableHtml.append(res);
                    bufferSize = tableInputStream.read(buffer, 0, 1024);
                }
                //把数据存在本地，sharePreferences保存的是没有解析的原网页
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("schedule", tableHtml.toString());
                editor.commit();
                tableInputStream.close();
                getTableConnection.disconnect();

                //与课表一样，得到成绩网页String类型的html
                getTest_url = getTest_url + userNumber + "&xm=%CB%D5%B6%AB%C9%FA&gnmkdm=N121605";
                getTestUrl = new URL(getTest_url);
                getTestConnection = (HttpURLConnection) getTestUrl.openConnection();
                getTestConnection.setRequestMethod("POST");
                getTestConnection.setDoOutput(true);
                getTestConnection.setDoInput(true);
                getTestConnection.setRequestProperty("Cookie", cookie);
                getTestConnection.setRequestProperty("Accept-Charset", "gbk");
                getTestConnection.setRequestProperty("Referer", "http://202.119.225.35/xs_main.aspx?xh=" + userNumber + "&xm=%EF%BF%BD%D5%B6%EF%BF%BD%EF%BF%BD%EF%BF%BD&gnmkdm=N121605");
                getTestConnection.connect();
                DataOutputStream Out = new DataOutputStream(
                        getTestConnection.getOutputStream());
                Out.writeBytes(postTestData);
                Out.flush();
                Out.close();
                testInputStream = getTestConnection.getInputStream();
                testHtml = new StringBuffer();
                int bufferSize2;
                byte[] buffer2 = new byte[1024];
                bufferSize2 = testInputStream.read(buffer2, 0, 1024);
                while (bufferSize2 != -1) {
                    String res = new String(buffer2, 0, bufferSize2, "gbk");
                    testHtml.append(res);
                    bufferSize2 = testInputStream.read(buffer2, 0, 1024);
                }
                testInputStream.close();
                getTestConnection.disconnect();
                if (Flag.equals("Schedule")) {
                    flaghandler.sendEmptyMessage(MSG_TABLE);
                } else if (Flag.equals("Test")){
                    flaghandler.sendEmptyMessage(MSG_TEST);
                }
            } catch (IOException e) {
                e.printStackTrace();
                flaghandler.sendEmptyMessage(ERR_NET2);
            }
        }
    }

    Handler flaghandler = new Handler() {
        public void handleMessage(Message msg) {
            if (msg.what == MSG_TABLE) {
                progressDialog.dismiss();
                Intent intent = new Intent(LoginActivity.this, ScheduleActivity.class);
                LoginActivity.this.startActivity(intent);
            } else if (msg.what == MSG_TEST) {
                progressDialog.dismiss();
                //解析网页，得到干净的有效数据全部存放在testString中，每一项用‘*’分隔，每一项里的绩点分数成绩什么的用‘&’分隔
                testString=jsoupTest.parse(testHtml.toString());
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("test", testString);
                editor.commit();
                Intent intent = new Intent(LoginActivity.this, TestActivity.class);
                intent.putExtra("testString",testString);
                LoginActivity.this.startActivity(intent);
            } else if (msg.what == ERR_NET) {
                Toast.makeText(LoginActivity.this, "网络出错了,请检查网络连接", Toast.LENGTH_LONG).show();
            } else if (msg.what == ERR_NET2) {
                Toast.makeText(LoginActivity.this, "网络出错了", Toast.LENGTH_LONG).show();
                progressDialog.dismiss();
            } else if (msg.what == ERR_CHECK) {
                progressDialog.dismiss();
                Toast.makeText(LoginActivity.this, "验证码不正确！！", Toast.LENGTH_LONG).show();
                new GetCheckCode().start();
            } else if (msg.what == ERR_PASS) {
                progressDialog.dismiss();
                new GetCheckCode().start();
                Toast.makeText(LoginActivity.this, "密码错误！！", Toast.LENGTH_LONG).show();
            } else if (msg.what == ERR_USER) {
                progressDialog.dismiss();
                new GetCheckCode().start();
                Toast.makeText(LoginActivity.this, "用户名不存在或未按照要求参加教学活动！！", Toast.LENGTH_LONG).show();
            }
        }
    };

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        switch (item.getItemId()) {
            case android.R.id.home:
                Intent intent =new Intent(LoginActivity.this,MainActivity.class);
                startActivity(intent);
                break;
            default:
                return super.onOptionsItemSelected(item);
        }
        return true;
    }
}