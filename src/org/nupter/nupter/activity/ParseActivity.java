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
        setContentView(R.layout.activity_main);
        ParseObject testObject = new ParseObject("TestObject");
        testObject.put("foo", "bar");
        testObject.saveInBackground();
    }
}