package org.nupter.nupter.activity;


import org.nupter.nupter.R;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.view.Window;
import android.view.WindowManager;

/**
 * 启动画面，闪屏效果
 *
 * @author panlei
 */

public class WelcomeActivity extends Activity {

    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_welcome);

        Start();
    }
    public void Start() {
                   new Thread() {
                                 public void run() {
                                        try {
                                               Thread.sleep(2000);
                                               } catch (InterruptedException e) {
                                               e.printStackTrace();
                                               }
                                  Intent intent = new Intent();
                                  intent.setClass(WelcomeActivity.this, MainActivity.class);
                                  startActivity(intent);
                                  finish();
                                  }
                    }.start();
   }

}
