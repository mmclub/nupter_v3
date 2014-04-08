package org.nupter.nupter.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;
import org.nupter.nupter.R;

import javax.crypto.Cipher;
import javax.crypto.SecretKey;
import javax.crypto.SecretKeyFactory;
import javax.crypto.spec.DESedeKeySpec;
import javax.crypto.spec.IvParameterSpec;
import javax.crypto.spec.SecretKeySpec;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.net.HttpURLConnection;
import java.net.URL;
import java.security.Key;

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

    final byte[] key192 = {(byte) 0x45, (byte) 0x8A, (byte) 0x91, (byte) 0x9E, (byte) 0x18, (byte) 0x5F, (byte) 0x3E, (byte) 0x11, (byte) 0xAE, (byte) 0xBC, (byte) 0xBA, (byte) 0x87, (byte) 0x59, (byte) 0x6E, (byte) 0x31, (byte) 0x01, (byte) 0x14, (byte) 0x82, (byte) 0xC3, (byte) 0x34, (byte) 0xFF, (byte) 0xBB, (byte) 0xEA, (byte) 0xD1};
    final byte[] keyiv = { };

    private String str;
    private String postData;
    private Button login;
    private ProgressDialog progressDialog;
    private HttpURLConnection loginConnection;
    private String login_url = "http://geek58.byethost6.com/test1.php";

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_schoolcard);
        login = (Button) findViewById(R.id.login_schoolCard);
        login.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                progressDialog = new ProgressDialog(LoginSchoolcardActivity.this);
                progressDialog.setMessage("正在登陆中。。。");
                progressDialog.setCanceledOnTouchOutside(false);
                progressDialog.show();
                str = "B11011319$*%&123456";
                try {
                    byte[] data = str.getBytes("UTF-8");
                    Log.i("TAG", "字符串:" + data.length);
                    byte[] encodeByte_CBC = des3EncodeCBC(data);
                    Log.i("TAG", "加密字符串:" + encodeByte_CBC.length);
                    postData = "userStudentIdPassword=" + Base64.encodeToString(encodeByte_CBC, Base64.DEFAULT);
                    Log.i("TAG", "加密后的字符串:" + postData);
                } catch (Exception e) {
                }
                progressDialog.dismiss();
                //        new Login().start();
            }
        });
    }

    public byte[] des3EncodeCBC(byte[] data) {
        try {
            Key deskey = null;
            DESedeKeySpec spec = new DESedeKeySpec(key192);
            SecretKeyFactory keyfactory = SecretKeyFactory.getInstance("desede");
            deskey = keyfactory.generateSecret(spec);

            Cipher cipher = Cipher.getInstance("desede" + "/CBC/PKCS5Padding");
            IvParameterSpec ips = new IvParameterSpec(keyiv);
            cipher.init(Cipher.ENCRYPT_MODE, deskey, ips);
            byte[] bOut = cipher.doFinal(data);

            return bOut;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return null;
    }

    public static String byte2hex(byte[] b) {
        String hs = "";
        String stmp = "";
        for (int n = 0; n < b.length; n++) {
            stmp = (java.lang.Integer.toHexString(b[n] & 0XFF));
            if (stmp.length() == 1)
                hs = hs + "0" + stmp;
            else
                hs = hs + stmp;
        }
        return hs.toUpperCase();
    }


    class Login extends Thread {
        public void run() {
            try {
                URL loginUrl = new URL(login_url);
                loginConnection = (HttpURLConnection) loginUrl.openConnection();
                loginConnection.setDoOutput(true);
                loginConnection.setDoInput(true);
                loginConnection.setRequestMethod("POST");
                loginConnection.setConnectTimeout(10000);
                loginConnection.setReadTimeout(10000);
                loginConnection.setUseCaches(false);
                loginConnection.connect();
                DataOutputStream out = new DataOutputStream(loginConnection.getOutputStream());
                out.writeBytes(postData);
                out.flush();
                out.close();

                InputStream inputStream = loginConnection.getInputStream();
                Log.i("string", getHtmlString(inputStream));
                inputStream.close();

                loginConnection.disconnect();
                flaghandler.sendEmptyMessage(MSG_SCHOOLCARD);
            } catch (IOException e) {
                flaghandler.sendEmptyMessage(ERR_NET);
            }
        }
    }

    public String getHtmlString(InputStream inputStream) {
        byte[] buffer = new byte[512];
        ByteArrayOutputStream outSteam = new ByteArrayOutputStream();
        String html = "";
        try {
            int bufferSize;
            while ((bufferSize = inputStream.read(buffer)) != -1) {
                outSteam.write(buffer, 0, bufferSize);
            }
            html = new String(outSteam.toByteArray(), "utf-8");
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
            } else if (msg.what == ERR_NET) {
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
}
