package org.nupter.nupter.activity;


import android.app.Activity;
import android.app.AlertDialog;
import android.content.DialogInterface;
import android.media.MediaPlayer;
import android.os.Bundle;
import org.nupter.nupter.R;


/**
 *
 *
 * @author panlei
 */

public class ClassAlarmActivity extends Activity {

    MediaPlayer alarmMusic;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        alarmMusic = MediaPlayer.create(this, R.raw.alarmmusic);
        alarmMusic.setLooping(true);
        // 播放音乐
        alarmMusic.start();
        new AlertDialog.Builder(ClassAlarmActivity.this)
                .setTitle("闹钟")
                .setMessage("要上课了，亲!!!")
                .setPositiveButton("确定",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog,
                                                int whichButton) {
                                // 停止音乐
                                alarmMusic.stop();
                                // 结束该Activity
                                System.exit(0);
                                android.os.Process.killProcess(android.os.Process.myPid());
                            }
                        }).show();
    }

    @Override
    public void onBackPressed() {
        alarmMusic.stop();
        // 结束该Activity
        System.exit(0);
        android.os.Process
                .killProcess(android.os.Process
                        .myPid());
    }
}
