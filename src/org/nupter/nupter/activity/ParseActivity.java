package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import com.parse.ParseObject;
import org.nupter.nupter.R;
import com.parse.Parse;
import com.parse.ParseAnalytics;

/**
 * 演示Parse.com SDK使用
 *
 * @author <a href="mailto:lxyweb@gmail.com">Lin xiangyu</a>
 *
 */
public class ParseActivity extends Activity {
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        Parse.initialize(this, "MqbhaZ0G5tX76IISHjbp4JekaYfyzIXWa0pdKHKv", "HF1zGCXpm5hfuvvCUS6taOmM0WiE6cLbhsUiI8JB");
        setContentView(R.layout.activity_main);
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}