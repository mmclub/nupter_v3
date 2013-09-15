package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.apache.http.HttpEntity;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.cookie.Cookie;
import org.apache.http.impl.client.DefaultHttpClient;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.NetUtils;

import java.io.ByteArrayOutputStream;
import java.io.InputStream;
import java.util.List;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 13-9-13
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public class LibraryLoginActivity extends Activity {
    private String user, pwd, postUrl = "";
    private EditText userEditText, pwdEditText;
    private Button libraryLogin;
    private String libraryCookie;
    private SharedPreferences userInit, pwdInit;
    private List<Cookie> cookies;

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_login);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        userEditText = (EditText) this.findViewById(R.id.libraryUser);
        pwdEditText = (EditText) this.findViewById(R.id.libraryPwd);
        libraryLogin = (Button) this.findViewById(R.id.libraryLogin);
        libraryLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                user = userEditText.getText().toString();
                Log.d("lib_user",user);
                pwd = pwdEditText.getText().toString();
                if (NetUtils.isNewworkConnected()) {
                    if (user.equals("")) {
                        Toast.makeText(LibraryLoginActivity.this, "帐号不能为空", Toast.LENGTH_SHORT).show();
                    } else if (pwd.equals("")) {
                        Toast.makeText(LibraryLoginActivity.this, "密码不能为空", Toast.LENGTH_SHORT).show();
                    } else {
                        postUrl = "http://202.119.228.6:8080/reader/redr_verify.php?select=cert_no&number=" + user + "&passwd=" + pwd + "&submit.x=17&submit.y=10";
                        Log.d("lib_postUrl", postUrl);
                        new LibraryCookie().start();

                    }
                } else {
                    Toast.makeText(LibraryLoginActivity.this, "网络还没连接哦", Toast.LENGTH_SHORT).show();
                }
            }
        });

    }

    class LibraryCookie extends Thread {
        public void run() {
            try {

                DefaultHttpClient httpClient = new DefaultHttpClient();
                HttpGet httpget = new HttpGet(postUrl);
                Log.d("lib_postUrl", postUrl);
                HttpResponse response = httpClient.execute(httpget);
                cookies = httpClient.getCookieStore().getCookies();
                Log.d("lib_cookie",cookies.toString());
                if (cookies.isEmpty()) {

                }

                HttpEntity entity = response.getEntity();
                InputStream inputStream = entity.getContent();
                String libBookHtml = getHtmlString(inputStream);
                Log.d("lib_book_re1", libBookHtml);
                if (libBookHtml.contains("密码错误")) {
                    Log.d("lib_login_error", "Failed");
                } else {
                    Intent intent = new Intent(LibraryLoginActivity.this, BookBorrowActivity.class);
                    intent.putExtra("cookieValue", cookies.get(cookies.size() - 1).getValue());
                    intent.putExtra("libCookie", "PHPSESSID=" + cookies.get(cookies.size() - 1).getValue());

                    startActivity(intent);
                }

            } catch (Exception e) {
                Log.d("lib_ex", e.toString());
            } finally {

            }
        }

        public String getHtmlString(InputStream inputStream) {
            byte[] buffer = new byte[1024];
            ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
            String html = "";
            try {
                int j = 0;
                int bufferSize;
                while ((bufferSize = inputStream.read(buffer)) != -1) {
                    outSteam.write(buffer, 0, bufferSize);
                }
                html = new String(outSteam.toByteArray(), "UTF-8");
                outSteam.close();
            } catch (Exception e) {
                Log.d("lib_book_ex", "None");
            }
            return html;
        }
    }


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
