package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import com.loopj.android.http.AsyncHttpClient;
import com.loopj.android.http.AsyncHttpResponseHandler;
import org.nupter.nupter.R;

/**
 * Created with IntelliJ IDEA.
 * User: mac
 * Date: 13-9-13
 * Time: 下午5:26
 * To change this template use File | Settings | File Templates.
 */
public class LibraryLoginActivity extends Activity {
    private String user, pwd, postUrl = "http://202.119.228.6:8080/reader/redr_verify.php?select=cert_no&number=110201200163000&passwd=B12041309&submit.x=49&submit.y=8";
    private EditText userEditText, pwdEditText;
    private Button libraryLogin;
    public void onCreate(Bundle savedInstanceState)
    {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_library_login);
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
        userEditText = (EditText) this.findViewById(R.id.libraryUser);
        pwdEditText = (EditText) this.findViewById(R.id.libraryPwd);
        user = userEditText.getText().toString();
        pwd = pwdEditText.getText().toString();
        libraryLogin = (Button) this.findViewById(R.id.libraryLogin);
        libraryLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                new AsyncHttpClient().post(postUrl, null,
                        new AsyncHttpResponseHandler() {
                            @Override
                            public void onSuccess(String response) {
                                Log.d("lib_re", response);

                            }
                        });
            }
        });
        new AsyncHttpClient().post(postUrl, null,
                new AsyncHttpResponseHandler() {
                    @Override
                    public void onSuccess(String response) {
                        Log.d("lib_re", response);

                    }
                });
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
