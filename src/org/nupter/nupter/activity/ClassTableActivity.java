package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.ant.liao.GifView;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import com.loopj.android.http.RequestParams;
import org.nupter.nupter.R;

import java.io.DataOutputStream;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.regex.Matcher;
import java.util.regex.Pattern;


/**
 * 课表板块主界面
 */

public class ClassTableActivity extends Activity {
    private GifView gifView;
    private String cookie = "";
    private String postData="";
    private HttpURLConnection connection,connection2;
    private InputStream inputStream;
    private EditText identify;
    private Button login;
    private String identify_number;
    private String defaul_url = "http://202.119.225.35/default2.aspx";
    private String checkcode_url = "http://202.119.225.35/CheckCode.aspx";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_classstable);
        identify=(EditText)findViewById(R.id.identify);
        login=(Button)findViewById(R.id.login);
        new GetIdentify().start();
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                identify_number=identify.getText().toString();
  /*              RequestParams params = new RequestParams();
                params.put("TextBox1", "B11040916");
                params.put("TextBox2", "445281199304282155");
                params.put("TextBox3",identify_number);
                new AsyncHttpClient().post("http://202.119.225.35/xs_main.aspx?xh=B11040916",params,new AsyncHttpResponseHandler(){
                    @Override
                    public void onSuccess(String s) {
                        Log.i("TAG",s);
                    }
                });      */
                postData = "__VIEWSTATE=dDwtMTg3MTM5OTI5MTs7PmemTdyOgz7iR3IwB6rzBV6MRdNi&TextBox1=B11040916&TextBox2=445281199304282155&TextBox3="+identify_number+"&RadioButtonList1=%D1%A7%C9%FA&Button1=&lbLanguage=";
                new GetTable().start();
            }
        });
        gifView = (GifView) findViewById(R.id.ImageView);


    }
    class GetTable extends Thread{
       URL loginUrl;
       public void run(){
           try {
               loginUrl = new URL(defaul_url);
               connection = (HttpURLConnection) loginUrl.openConnection();
               connection.setDoOutput(true);
               connection.setDoInput(true);
               connection.setRequestMethod("POST");
               connection.setConnectTimeout(10000);
               connection.setReadTimeout(10000);
               connection.setUseCaches(false);
               connection.connect();
               DataOutputStream out = new DataOutputStream(
                       connection.getOutputStream());
               out.writeBytes(postData);

               int chByte1 = 0;
               byte[] bin = new byte[512];
               InputStream inpost = connection.getInputStream();

               chByte1 = inpost.read(bin);
               String s = new String(bin, 0, chByte1, "gbk");
               Pattern patpost = Pattern.compile("xs_main");
               Matcher matpost = patpost.matcher(s);
               if (matpost.find()) {
                   Log.i("TAG","success");
               }
               Log.i("str", s);
           } catch (Exception e){

           }

       }
    }
    class GetIdentify extends Thread {
        public void run() {
            URL loginUrl;
            try {
                loginUrl = new URL(defaul_url);
                connection = (HttpURLConnection) loginUrl.openConnection();
                connection.setDoOutput(true);
                connection.setDoInput(true);
                connection.setRequestMethod("GET");
                connection.setConnectTimeout(10000);
                connection.setReadTimeout(10000);
                connection.setUseCaches(false);
                connection.connect();
                String key = "";
                if (connection != null) {
                    for (int i = 1; (key = connection.getHeaderFieldKey(i)) != null; i++) {
                        if (key.equalsIgnoreCase("set-cookie")) {
                            cookie = connection.getHeaderField(key);
                            cookie = cookie.substring(0,
                                    cookie.indexOf(";"));
                        }
                    }
                }
                connection.disconnect();
                URL getUrl = new URL(checkcode_url);
                connection2 = (HttpURLConnection) getUrl.openConnection();
                connection2.setRequestProperty("Cookie", cookie);
                connection2.setRequestProperty("Accept-Charset", "gbk");
                connection2.connect();

                inputStream = connection2.getInputStream();
                gifView.setGifImage(inputStream);
                gifView.setGifImageType(GifView.GifImageType.COVER);
            } catch (Exception e) {
            }
        }
    }
}