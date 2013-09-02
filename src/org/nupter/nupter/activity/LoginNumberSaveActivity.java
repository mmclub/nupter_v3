package org.nupter.nupter.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;
import org.nupter.nupter.MyApplication;
import org.nupter.nupter.R;

/**
 * Created with IntelliJ IDEA.
 * User: helloworld
 * Date: 13-9-1
 * Time: 上午11:03
 * To change this template use File | Settings | File Templates.
 */
public class LoginNumberSaveActivity extends Activity {
    private EditText username;
    private EditText password;
    private Button save;
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login_number_save);
        username = (EditText) findViewById(R.id.user);
        password = (EditText) findViewById(R.id.pwd);
        save = (Button) findViewById(R.id.save);
        save.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if (event.getAction() == MotionEvent.ACTION_DOWN) {
                    v.setBackgroundResource(R.drawable.save_btn_pressed);
                } else if (event.getAction() == MotionEvent.ACTION_UP) {
                    v.setBackgroundResource(R.drawable.save_btn);
                }
                return false;
            }
        });
        save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MyApplication.getAppContext());
                SharedPreferences.Editor editor = preferences.edit();
                editor.putString("user", username.getText().toString());
                editor.putString("password", password.getText().toString());
                editor.commit();
                Toast.makeText(LoginNumberSaveActivity.this, "账号设置成功", Toast.LENGTH_LONG).show();
            }
        });
        this.getActionBar().setDisplayHomeAsUpEnabled(true);
    }
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
