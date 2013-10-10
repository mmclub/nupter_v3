package org.nupter.nupter.activity;


import android.app.Activity;
import android.app.Service;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.os.Vibrator;
import android.util.Log;
import android.view.*;
import android.widget.Button;
import org.nupter.nupter.R;


/**
 *
 *
 * @author panlei
 */

public class ClassAlarmActivity extends Activity {

    MediaPlayer alarmMusic;
    Button sureButton;
    HomeKeyEventBroadCastReceiver receiver;
    Vibrator mVibrator;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);//去掉标题栏
        setContentView(R.layout.activity_class_alarm);
        receiver = new HomeKeyEventBroadCastReceiver();
        registerReceiver(receiver, new IntentFilter(Intent.ACTION_CLOSE_SYSTEM_DIALOGS));
        sureButton = (Button)findViewById(R.id.sureButton);
        alarmMusic = MediaPlayer.create(this, R.raw.alarmmusic);
        alarmMusic.setLooping(true);
        // 播放音乐
        alarmMusic.start();
        //获取振动器
        mVibrator = (Vibrator)ClassAlarmActivity.this.getSystemService(Service.VIBRATOR_SERVICE);
        //控制震动周期，repeat=-1时，震动只出现一次；repeat=0时，震动一直持续，必须手动取消
        mVibrator.vibrate(new long[] { 500, 1000, 500, 1000}, 0);


        sureButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                              //停止音乐
                              alarmMusic.stop();
                              mVibrator.cancel();
                              // 结束该Activity
                               System.exit(0);
                               android.os.Process.killProcess(android.os.Process.myPid());
            }
        });
        ClassAlarmActivity.this.setFinishOnTouchOutside(false);
    }

    class HomeKeyEventBroadCastReceiver extends BroadcastReceiver {
        static final String SYSTEM_REASON = "reason";
        static final String SYSTEM_HOME_KEY = "homekey";
        @Override
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            if (action.equals(Intent.ACTION_CLOSE_SYSTEM_DIALOGS)) {
                String reason = intent.getStringExtra(SYSTEM_REASON);
                if (reason != null) {
                    if (reason.equals(SYSTEM_HOME_KEY)) {
                        alarmMusic.stop();
                        mVibrator.cancel();
                        // 结束该Activity
                        System.exit(0);
                        android.os.Process
                                .killProcess(android.os.Process
                                        .myPid());
                    }
                }
            }
        }
    }

   /* @Override
    public void onBackPressed() {
        alarmMusic.stop();
        // 结束该Activity
        System.exit(0);
        android.os.Process
                .killProcess(android.os.Process
                        .myPid());
    }    */
}
