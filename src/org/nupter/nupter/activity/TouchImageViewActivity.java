package org.nupter.nupter.activity;

import android.app.Activity;
import android.os.Bundle;
import org.nupter.nupter.ui.TouchImageView;


public class TouchImageViewActivity extends Activity {
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        TouchImageView img = new TouchImageView(this);
        setContentView(img);
    }
}