package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import org.nupter.nupter.R;
import org.nupter.nupter.data.MessageRecord;
import org.nupter.nupter.utils.Log;

import java.util.List;


/**
 * SugarDB用法演示
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 * 请学习  http://satyan.github.io/sugar/getting-started.html
 */

public class SugarDBActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        MessageRecord msg = new MessageRecord(this, "移动互联网俱乐部招新啦", "点击nupter.org报名");
        Log.d( msg.toString());
        msg.save();

    }
}