package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import org.nupter.nupter.R;
import org.nupter.nupter.utils.Weather;


/**
 * 主界面Activity
 */

public class MainActivity extends Activity {

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }
}
